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


import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static org.sephire.games.framework4x.core.utils.ResourceLoading.normalizePackageNameForReflection;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"pluginName"})
public class PluginSpec implements Comparable<PluginSpec> {
	private String pluginName;
	private String rootPackage;
	private Option<String> parentPlugin;
	private final static Pattern PROPERTIES_BUNDLE_FORMAT = Pattern.compile("^(?<package>.*)/(?<bundleName>[^_]*)(_.*){0,2}\\.properties$");

	/**
	 * Check whether the plugin is a base plugin or not.
	 * It boils down to whether it has a defined parent plugin.
	 * @return
	 */
	public boolean isBasePlugin() {
		return parentPlugin.isEmpty();
	}

	@Override
	public int compareTo(PluginSpec o) {
		return pluginName.compareTo(o.pluginName);
	}

	/**
	 * <p>Get the full title of the plugin, looking it up in its i18n resource files.</p>
	 * <p>The resource bundle containing the i18n values for this information must exist</p>
	 * @param locale
	 * @return
	 */
	public Try<String> getTitle(Locale locale) {
		return Try.of(() -> {
			var keyName = String.format("%s.plugin.title", getPluginName());
			var descriptionLookup = lookupI18nResourceInBundles(locale, keyName).getOrElseThrow((t) -> t);
			return descriptionLookup
			  .getOrElseThrow(() -> new InvalidPluginException("The plugin's title has not been found among its resource files"));
		});
	}

	/**
	 * <p>Get the full description of the plugin, looking it up in its i18n resource files.</p>
	 * <p>The resource bundle containing the i18n values for this information must exist</p>
	 * @param locale
	 * @return
	 */
	public Try<String> getDescription(Locale locale) {
		return Try.of(() -> {
			var keyName = String.format("%s.plugin.description", getPluginName());
			var descriptionLookup = lookupI18nResourceInBundles(locale, keyName).getOrElseThrow((t) -> t);
			return descriptionLookup
			  .getOrElseThrow(() -> new InvalidPluginException("The plugin's description has not been found among its resource files"));
		});
	}

	/**
	 * Given a key and a locale to lookup, it will search among all the plugin's properties bundles to find
	 * the i18n resource identified by the key.
	 * @param locale
	 * @param key
	 * @return
	 */
	private Try<Option<String>> lookupI18nResourceInBundles(Locale locale, String key) {
		// We must iterate over each resource bundle, because we don't want the user to use a specific named
		// resource bundle to define this
		return Try.of(() -> getAvailableI18NResourceBundles().map((name) -> ResourceBundle.getBundle(name, locale))
		  .find((bundle) -> bundle.containsKey(key))
		  .map((bundle) -> bundle.getString(key))
		);
	}

	/**
	 * Get a list of available properties bundles base names in the plugin.
	 * @return
	 */
	private Set<String> getAvailableI18NResourceBundles() {
		Reflections reflections = new Reflections(
		  normalizePackageNameForReflection(getRootPackage().concat(".i18n")),
		  new ResourcesScanner());

		return HashSet.ofAll(reflections.getResources(Pattern.compile(".*\\.properties")))
		  .map((name) -> {
			  var matcher = PROPERTIES_BUNDLE_FORMAT.matcher(name);
			  matcher.find();
			  return matcher.group("package") + "/" + matcher.group("bundleName");
		  });
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String rootPackage;
		private String pluginName;
		private Option<String> parentPlugin;

		public Builder withRootPackage(String rootPackage) {
			this.rootPackage = rootPackage;
			return this;
		}

		public Builder withPluginName(String pluginName) {
			this.pluginName = pluginName;
			return this;
		}

		public Builder withParent(String parentPlugin) {
			this.parentPlugin = Option.of(parentPlugin);
			return this;
		}

		public Builder withParent(Option<String> parentPlugin) {
			this.parentPlugin = parentPlugin;
			return this;
		}

		public Try<PluginSpec> build() {
			return Try.of(() -> {
				if (parentPlugin == null) {
					parentPlugin = Option.none();
				}

				if (rootPackage == null || pluginName == null) {
					pluginName = pluginName == null ? "unspecified" : pluginName;
					throw new InvalidPluginSpecException(pluginName, "rootPackage and pluginName are mandatory parameters");
				}

				var pluginSpec = new PluginSpec(pluginName, rootPackage, parentPlugin);

				if (!doesRootPackageExist(pluginSpec)) {
					throw new InvalidPluginSpecException(pluginName, "the root package hasn't been found in the classpath");
				}

				// This checks the availability of basic i18n metadata inside the plugin
				pluginSpec.getTitle(Locale.ENGLISH)
				  .andThen(() -> pluginSpec.getDescription(Locale.ENGLISH))
				  .getOrElseThrow((t) -> t);

				return pluginSpec;
			});
		}

		/**
		 * Checks whether the root package declared for the plugin exists in the classpath.
		 * @param pluginSpec
		 * @return
		 */
		private static boolean doesRootPackageExist(PluginSpec pluginSpec) {
			var packageFolder = pluginSpec.getRootPackage().replaceAll("\\.", "/");
			var i18nFolder = "i18n/".concat(pluginSpec.getPluginName());
			var standardResourcesFolderExists = ClassLoader.getSystemClassLoader().getResource(packageFolder) != null;
			var i18nResourcesFolderExists = ClassLoader.getSystemClassLoader().getResource(i18nFolder) != null;

			return standardResourcesFolderExists || i18nResourcesFolderExists;
		}
	}
}
