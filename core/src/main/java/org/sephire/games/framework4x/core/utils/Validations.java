package org.sephire.games.framework4x.core.utils;

import io.vavr.collection.List;
import io.vavr.control.Try;

/**
 * A class to hold convenience parameter validation methods
 */
public class Validations {

	/**
	 * <p>Check whether the given list of arguments contains a null object,
	 * in which case the method is a failure with an IllegalArgumentException,
	 * which is the standard way to handle bad parameters in this application.</p>
	 * <p>The exception contains which argument (identified by position) is illegal</p>
	 *
	 * @param arguments
	 * @return
	 */
	public static Try<Void> areArgumentsNotNull(Object... arguments) {
		return Try.of(()->{
			var nullArgument = List.of(arguments)
			  .zipWithIndex()
			  .find(argumentTuple->argumentTuple._1 == null);

			if(nullArgument.isDefined()){
				throw new IllegalArgumentException(String.format("The argument %d is null",nullArgument.get()._2));
			}

			return null;
		});
	}

}
