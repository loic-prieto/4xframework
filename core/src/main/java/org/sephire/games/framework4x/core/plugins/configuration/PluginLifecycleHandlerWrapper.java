package org.sephire.games.framework4x.core.plugins.configuration;

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.String.format;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;


/**
 * This internal class wraps a plugin lifecycle handler provided by the plugin author.
 * This is needed because such a handler has a general freeform class structure and method
 * signature, due to the use of annotations to declare that class as a handler.
 *
 * This wrapper allows the core framework to more easily use that handle.
 */
public class PluginLifecycleHandlerWrapper {
	private Object pluginLifecycleHandlerInstance;
	private Option<Method> pluginLoadingHook;
	private Option<Method> gameLoadingHook;


	private PluginLifecycleHandlerWrapper(Object pluginLifecycleHandlerInstance,Option<Method> pluginLoadingHook, Option<Method> gameLoadingHook) {
		this.pluginLifecycleHandlerInstance = pluginLifecycleHandlerInstance;
		this.pluginLoadingHook = pluginLoadingHook;
		this.gameLoadingHook = gameLoadingHook;
	}

	/**
	 * Builder for the PluginLifecycleHandlerWrapper, makes sure that the class is a valid
	 * lifecycle handler as designed.
	 *
	 * Will return a failure with error {@link InvalidPluginLifecycleHandlerException} if not.
	 *
	 * @param pluginLifecycleHandlerClass
	 * @return
	 */
	public static Try<PluginLifecycleHandlerWrapper> from(Class<?> pluginLifecycleHandlerClass) {
		return Try.of(()->{
			Object instance = null;
			try {instance = pluginLifecycleHandlerClass.getConstructor().newInstance();}
			catch(Throwable t) {
				throw new InvalidPluginLifecycleHandlerException(pluginLifecycleHandlerClass,
				  format("Error instantiating the class: %s", t.getMessage()));
			}

			var pluginLoadingHook = fetchPluginLoadingHookMethod(pluginLifecycleHandlerClass);
			if(pluginLoadingHook.isFailure()){
				throw pluginLoadingHook.getCause();
			}

			var gameLoadingHook = fetchGameLoadingHookMethod(pluginLifecycleHandlerClass);
			if(gameLoadingHook.isFailure()){
				throw gameLoadingHook.getCause();
			}

			return new PluginLifecycleHandlerWrapper(instance,pluginLoadingHook.get(),gameLoadingHook.get());
		});
	}

	/**
	 * Call the plugin load hook with the current configuration object loaded.
	 *
	 * This method should be called only after parent plugins have been loaded and after
	 * all the plugin's resource provider and automatic resources have been charged, since
	 * this should be the last step of the plugin's load process.
	 *
	 * Won't do anything if no plugin loading hook has been defined for this plugin.
	 *
	 * May return standard java errors when dynamically calling methods with reflection:
	 * - IllegalAccessException
	 * - InvocationTargetException
	 * Both wrapped in a RuntimeException (inside the Try object)
	 *
	 * @param configuration
	 * @return
	 */
	public Try<Void> callPluginLoadingHook(Configuration.Builder configuration) {
		return callHook(pluginLoadingHook,configuration);
	}

	/**
	 * Call the game load hook with the current configuration object loaded.
	 *
	 * This method should be called only after parent plugins have been called.
	 *
	 * Won't do anything if no game loading hook has been defined for this plugin.
	 *
	 * May return standard java errors when dynamically calling methods with reflection:
	 * - IllegalAccessException
	 * - InvocationTargetException
	 * Both wrapped in a RuntimeException (inside the Try object)
	 *
	 * @param configuration
	 * @return
	 */
	public Try<Void> callGameLoadingHook(Configuration.Builder configuration) {
		return callHook(gameLoadingHook,configuration);
	}

	private Try<Void> callHook(Option<Method> hookMethod, Configuration.Builder configuration) {
		return Try.of(()->{
			hookMethod.peek((hook)->{
				try { hook.invoke(pluginLifecycleHandlerInstance,configuration); }
				catch(IllegalAccessException | InvocationTargetException e) { throw new RuntimeException(e); }
			});

			return null;
		});
	}

	/**
	 * Fetches the plugin loading hook method from the handler class if it exists.
	 * Validates it has the correct signature and that there is only one.
	 *
	 * May return the following errors:
	 * - {@link InvalidPluginLifecycleHandlerException}
	 *
	 * @param pluginLifecycleHandlerClass
	 * @return
	 */
	private static Try<Option<Method>> fetchPluginLoadingHookMethod(Class<?> pluginLifecycleHandlerClass) {
		return fetchMethodWithAnnotation(pluginLifecycleHandlerClass,PluginLoadingHook.class);
	}

	/**
	 * Fetches the game loading hook method from the handler class if it exists.
	 * Validates it has the correct signature and that there is only one.
	 *
	 * May return the following errors:
	 * - {@link InvalidPluginLifecycleHandlerException}
	 *
	 * @param pluginLifecycleHandlerClass
	 * @return
	 */
	private static Try<Option<Method>> fetchGameLoadingHookMethod(Class<?> pluginLifecycleHandlerClass) {
		return fetchMethodWithAnnotation(pluginLifecycleHandlerClass,GameLoadingHook.class);
	}

	/**
	 * Fetches a single method with the specified annotation and verifies it conforms to the signature:
	 * Configuration.Builder -> Try[Void]
	 *
	 * May return the following errors:
	 * - {@link InvalidPluginLifecycleHandlerException}
	 *
	 * @param pluginLifecycleHandlerClass
	 * @param annotation
	 * @return
	 */
	private static Try<Option<Method>> fetchMethodWithAnnotation(Class<?> pluginLifecycleHandlerClass, Class<? extends Annotation> annotation) {
		return Try.of(()->{
			var methods = getAllMethods(pluginLifecycleHandlerClass,withAnnotation(annotation));

			if(methods.size() > 1) {
				throw new InvalidPluginLifecycleHandlerException(pluginLifecycleHandlerClass,
				  format("Too many hook methods for %s, there can only be one",annotation.getName()));
			}
			if(methods.isEmpty()) {
				return Option.none();
			}

			var method = methods.iterator().next();
			if(method.getParameterCount() != 1 && !method.getParameters()[0].getType().isAssignableFrom(Configuration.Builder.class)){
				throw new InvalidPluginLifecycleHandlerException(pluginLifecycleHandlerClass,
				  format("The hook %s must have one parameter, and must be of type Configuration.Builder",annotation.getName()));
			}
			if(!method.getReturnType().isAssignableFrom(Try.class)) {
				throw new InvalidPluginLifecycleHandlerException(pluginLifecycleHandlerClass,
				  format("The hook %s can only return a Try object",annotation.getName()));
			}

			return Option.of(method);
		});
	}
}