package org.sephire.games.framework4x.core.model.game.parameter;

/**
 * A game parameter may be from a custom java type. That type needs to implement this interface,
 * so that the framework can convert it from a String, which will be what the user will provide.
 */
public interface CustomGameParameterType {

	static <TYPE> TYPE fromString(String value) { return null;}
}
