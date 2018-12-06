package org.sephire.games.framework4x.clients.terminal.api.config;

import java.util.Map;

/**
 * Configuration value related to the terrain mappings.
 * Used by yacl4j.
 */
public interface TerrainsMapping {
	Map<String, TerrainMapping> getMappings();
}
