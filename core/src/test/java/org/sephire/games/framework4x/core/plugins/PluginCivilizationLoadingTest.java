package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.utils.I18NString;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluginCivilizationLoadingTest {
	private static final String TEST1_PLUGIN_NAME = "org.sephire.games.framework4x.testing.testPlugin1";

	@Test
	@DisplayName("When loading a plugin, civilizations should be loaded if present in the plugin")
	public void should_load_civilizations_if_present() {
		var pluginSpec = PluginSpec.builder()
		  .withPluginName(TEST1_PLUGIN_NAME)
		  .withRootPackage(TEST1_PLUGIN_NAME)
		  .build();
		assertTrue(pluginSpec.isSuccess());

		var configuration = Configuration.builder();
		var pluginLoadTry = Plugin.from(pluginSpec.get(), configuration);

		assertTrue(pluginLoadTry.isSuccess());
		assertTrue(configuration.getConfig(CoreConfigKeyEnum.CIVILIZATIONS).isDefined());

		var civilizations = (Map<String, Civilization>) configuration
		  .getConfig(CoreConfigKeyEnum.CIVILIZATIONS, Map.class)
		  .get().get();

		assertTrue(civilizations.containsKey("test.civilization1"));
		var actualCivilization = civilizations.get("test.civilization1").get();

		assertEquals(theTestingCivilization(), actualCivilization);
	}

	private Civilization theTestingCivilization() {
		return new Civilization("test.civilization1",
		  I18NString.builder()
			.withEntry(Locale.ENGLISH, "Civilization 1")
			.withEntry(new Locale("es"), "Civilización 1")
			.build().get(),
		  I18NString.builder()
			.withEntry(Locale.ENGLISH, "tester")
			.withEntry(new Locale("es"), "testeador")
			.build().get(),
		  I18NString.builder()
			.withEntry(Locale.ENGLISH, "testers")
			.withEntry(new Locale("es"), "testeadores")
			.build().get(),
		  I18NString.builder()
			.withEntry(Locale.ENGLISH, "A test civilization")
			.withEntry(new Locale("es"), "Una civilización de test")
			.build().get()
		);
	}
}
