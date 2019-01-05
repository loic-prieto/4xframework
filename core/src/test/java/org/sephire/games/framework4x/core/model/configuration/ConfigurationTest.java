package org.sephire.games.framework4x.core.model.configuration;

import io.vavr.collection.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurationTest {

	@Test
	@DisplayName("Given a valid I18N resource, it should translate it correctly")
	public void should_translate_correctly() {
		var playerName = "testName";
		var civilizationName = "Sumeria";
		var i18nValue = "Hello {0}, your civilization is {1}";

		Configuration.Builder configurationBuilder = Configuration.builder()
		  .putConfig(CoreConfigKeyEnum.I18N,
			HashMap.of(Locale.ENGLISH,
			  HashMap.of("testKey", i18nValue)));

		var configuration = configurationBuilder.build();

		var expectedTranslation = "Hello testName, your civilization is Sumeria";
		var translation = configuration.getTranslationFor(Locale.ENGLISH, "testKey", playerName, civilizationName);
		assertTrue(translation.isDefined());
		assertEquals(expectedTranslation, translation.get());
	}
}
