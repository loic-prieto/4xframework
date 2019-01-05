package org.sephire.games.framework4x.core.plugins;

import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluginSpecTest {

	private static final String TEST_PLUGIN_WITH_I18N_NAME = "org.sephire.games.framework4x.testing.testPluginWithI18NResources";
	private static final String TEST_PLUGIN_WITHOUT_I18N_NAME = "org.sephire.games.framework4x.testing.testPluginWithoutI18N";

	@Test
	@DisplayName("Given a plugin spec, it should retrieve its title and description for a given locale")
	public void should_retrieve_plugin_title_and_description() {
		var pluginSpec = new PluginSpec(TEST_PLUGIN_WITH_I18N_NAME, TEST_PLUGIN_WITH_I18N_NAME, Option.none());

		var expectedTitle = "Test plugin with I18N resources";
		var expectedDescription = "A test plugin that contains an i18n resource so that its name and description can be retrieved";
		var title = pluginSpec.getTitle(Locale.ENGLISH);
		var description = pluginSpec.getDescription(Locale.ENGLISH);

		assertTrue(title.isSuccess());
		assertTrue(description.isSuccess());
		assertEquals(expectedTitle, title.get());
		assertEquals(expectedDescription, description.get());
	}

	@Test
	@DisplayName("If a plugin doest not have basic i18n metadata, it should be invalid")
	public void if_basic_plugin_i18n_is_not_provided_complain() {
		var pluginSpec = new PluginSpec(TEST_PLUGIN_WITHOUT_I18N_NAME, TEST_PLUGIN_WITHOUT_I18N_NAME, Option.none());
		var titleFetchTry = pluginSpec.getTitle(Locale.ENGLISH);

		assertTrue(titleFetchTry.isFailure());
		assertTrue(InvalidPluginException.class.isAssignableFrom(titleFetchTry.getCause().getClass()));
	}


}
