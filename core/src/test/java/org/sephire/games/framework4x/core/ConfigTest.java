package org.sephire.games.framework4x.core;

import com.yacl4j.core.ConfigurationBuilder;
import com.yacl4j.core.source.optional.ConfigurationSourceNotAvailableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.plugins.configuration.PluginSpecMapping;

public class ConfigTest {

	@Test
	public void testConfigLoad() {
		var mappingsFileLocation = "org/sephire/games/framework4x/testing/dummyPlugin/plugin.yaml";
		PluginSpecMapping mappings = null;
		try {
			mappings = ConfigurationBuilder.newBuilder()
			  .source().fromFileOnClasspath(mappingsFileLocation)
			  .build(PluginSpecMapping.class);
			Assertions.assertTrue(true);
		} catch (ConfigurationSourceNotAvailableException e) {
			Assertions.assertTrue(false);
		} catch (Throwable t) {
			Assertions.assertTrue(false);
		}

		Assertions.assertTrue(true);
	}
}
