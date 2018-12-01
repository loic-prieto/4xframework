package org.sephire.games.framework4x.core.plugins;

import lombok.Getter;

/**
 * An enumeration of all core resources types, to be loaded automatically
 * when initializing a plugin.
 **/
public enum CoreResourcesTypes {

	/**
	 * Data related to the terrain types
	 **/
	TERRAIN_TYPES("terrains-types.yaml");

	@Getter
	private String fileName;

	CoreResourcesTypes(String fileName) {
		this.fileName = fileName;
	}
}
