package org.sephire.games.framework4x.core.model.config;

public enum CoreConfigKeyEnum implements ConfigKeyEnum {
	/**
	 * This key holds the value for the configuration of terrain types.
	 * This are to be defined by at least base plugins.
	 */
	TERRAIN_TYPES,
	/**
	 * This key is to be used to store the game parameters as defined by the different plugins and
	 * given value by the user when creating a game.
	 */
	GAME_PARAMETERS;
}
