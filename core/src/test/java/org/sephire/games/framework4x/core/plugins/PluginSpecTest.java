/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.core.plugins;

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
		var pluginSpecTry = PluginSpec.builder()
		  .withRootPackage(TEST_PLUGIN_WITH_I18N_NAME)
		  .withPluginName(TEST_PLUGIN_WITH_I18N_NAME)
		  .build();

		assertTrue(pluginSpecTry.isSuccess());
		var pluginSpec = pluginSpecTry.get();

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
		var pluginSpecTry = PluginSpec.builder()
		  .withPluginName(TEST_PLUGIN_WITHOUT_I18N_NAME)
		  .withRootPackage(TEST_PLUGIN_WITHOUT_I18N_NAME)
		  .build();

		assertTrue(pluginSpecTry.isFailure());
		assertTrue(InvalidPluginException.class.isAssignableFrom(pluginSpecTry.getCause().getClass()));
	}

	@Test
	@DisplayName("If a plugin declares an invalid root package, it should be invalid")
	public void if_plugin_has_nonexistent_root_package_complain() {
		var pluginSpecTry = PluginSpec.builder()
		  .withPluginName(TEST_PLUGIN_WITH_I18N_NAME)
		  .withRootPackage("non.existent.root.package")
		  .build();

		assertTrue(pluginSpecTry.isFailure());
		assertTrue(InvalidPluginSpecException.class.isAssignableFrom(pluginSpecTry.getCause().getClass()));
	}

}
