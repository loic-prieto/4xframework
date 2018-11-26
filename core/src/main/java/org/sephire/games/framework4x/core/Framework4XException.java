package org.sephire.games.framework4x.core;

/**
 * Base 4X Framework exception.
 *
 * It is a runtime exception.
 */
public abstract class Framework4XException extends Throwable {

    public Framework4XException() {
    }

    public Framework4XException(String message) {
        super(message);
    }

    public Framework4XException(String message, Throwable cause) {
        super(message, cause);
    }

    public Framework4XException(Throwable cause) {
        super(cause);
    }

}
