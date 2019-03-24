package org.sephire.games.framework4x.clients.terminal.utils;

import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

/**
 * <p>This exception is thrown when performing a compare operation on an entity but
 * the compare operation itself does not make sense, based on the entity being compared.</p>
 * <p>For example, for integer ranges, the compare operation is not defined when a range
 * contains another if the sorting algorithm is based on checking the position on the
 * X axis.</p>
 */
public class IllegalCompareOperationException extends FourXFrameworkClientException {
	public IllegalCompareOperationException(String s) {
		super(s);
	}
}
