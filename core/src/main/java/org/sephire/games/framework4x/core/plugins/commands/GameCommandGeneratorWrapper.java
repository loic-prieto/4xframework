package org.sephire.games.framework4x.core.plugins.commands;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.GameCommand;
import org.sephire.games.framework4x.core.model.game.GameCommandCategory;

import java.lang.reflect.Method;

/**
 * <p>Wraps a game command generator method to provide a convenient calling interface</p>
 * <p>Take a look at {@link GameCommandGenerator} to check the signatures of the command generator</p>
 */
public class GameCommandGeneratorWrapper {

	private Method gameCommandGenerator;

	private GameCommandGeneratorWrapper(Method generator) {
		this.gameCommandGenerator = generator;
	}

	public static Try<GameCommandGeneratorWrapper> from(Method generatorMethod) {
		return Try.of(()->{
			if(!hasValidSignature(generatorMethod)){
				throw new GameCommandGeneratorInvalidSignatureException(generatorMethod);
			}
			if(!hasValidConstructor(generatorMethod)) {
				throw new NoDefaultConstructorException(generatorMethod.getDeclaringClass());
			}

			return new GameCommandGeneratorWrapper(generatorMethod);
		});
	}

	/**
	 * <p>Executes the game command generator and puts the result into the configuration</p>
	 * <p>As defined in {@link GameCommandGenerator} there may be three return types that are
	 * taken into account:<ul>
	 *     <li>GameCommand</li>
	 *     <li>GameCommandCategory</li>
	 *     <li>List&lt;GameCommandCategory&gt;</li>
	 * </ul>
	 * </p>
	 * <p>May return one the following errors:<ul>
	 *     <li>{@link GameCommandGeneratorInvalidSignatureException} if the return type is invalid</li>
	 *     <li>{@link org.sephire.games.framework4x.core.model.game.ParentCategoryDoesntExistException}
	 *     if the parent category of the command or category generated doesn't exist yet</li>
	 * </ul></p>
	 * @param configuration
	 * @return
	 */
	public Try<Void> execute(Configuration.Builder configuration) {
		return Try.of(()->{
			var neutralObject = gameCommandGenerator.getDeclaringClass().getConstructor().newInstance();

			Try<Object> resultTry = (Try<Object>)gameCommandGenerator.invoke(neutralObject,configuration);
			Object rawResult = resultTry.getOrElseThrow(t->t);

			if(List.class.isAssignableFrom(rawResult.getClass())) {
				Try.sequence(((List<GameCommandCategory>)rawResult).map(configuration::addGameCommandCategory))
				  .getOrElseThrow(t->t);
			} else if(java.util.List.class.isAssignableFrom(rawResult.getClass())) {
				Try.sequence(List.ofAll((java.util.List<GameCommandCategory>)rawResult).map(configuration::addGameCommandCategory))
				  .getOrElseThrow(t->t);
			} else if(GameCommand.class.isAssignableFrom(rawResult.getClass())) {
				configuration.addGameCommand((GameCommand)rawResult).getOrElseThrow(t->t);
			} else if(GameCommandCategory.class.isAssignableFrom(rawResult.getClass())) {
				configuration.addGameCommandCategory((GameCommandCategory)rawResult).getOrElseThrow(t->t);
			} else {
				throw new GameCommandGeneratorInvalidSignatureException(gameCommandGenerator);
			}

			return null;
		});
	}

	private static boolean hasValidSignature(Method generatorMethod){
		if(!generatorMethod.getReturnType().equals(Try.class)) return false;
		if(generatorMethod.getParameterCount() != 1) return false;
		if(!generatorMethod.getParameterTypes()[0].equals(Configuration.Builder.class)) return false;

		return true;
	}

	private static boolean hasValidConstructor(Method generatorMethod) {
		var isValid = true;
		try {
			generatorMethod.getDeclaringClass().getConstructor();
		} catch(NoSuchMethodException e) {
			isValid = false;
		}

		return isValid;
	}
}
