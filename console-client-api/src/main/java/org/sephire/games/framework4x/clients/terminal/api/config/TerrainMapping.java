package org.sephire.games.framework4x.clients.terminal.api.config;

/**
 * Configuration value related to the mapping between a terrain type and its
 * character and color in the terminal client.
 * Used by yacl4j.
 */
public interface TerrainMapping {

	/**
	 * The unicode character to show on the console representing this terrain type.
	 * @return
	 */
	String getCharacter();

	/**
	 * The color to use to paint the character.
	 * @return
	 */
	String getColor();
}
