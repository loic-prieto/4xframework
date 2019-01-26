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
