package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

import java.lang.reflect.Method;

import static java.lang.String.format;

/**
 * <p>
 * This exception is thrown when a dynamic method is called in the code, but fails
 * due to the usual standard errors:
 *   <ul>
 *       <li>IllegalAccessException</li>
 *       <li>IllegalArgumentException</li>
 *       <li>InvocationTargetException</li>
 *   </ul>
 * </p>
 *
 *
 */
@Getter
public class InvalidMethodInvocationException extends Framework4XException {
	private Throwable cause;
	private Method method;

	public InvalidMethodInvocationException(Method method, Throwable cause) {
		super(format("The calling of the method %s failed due to %s",method.getName(),cause.getMessage()),cause);
		this.cause = cause;
		this.method = method;
	}
}
