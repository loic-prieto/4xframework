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
@EqualsAndHashCode(of={"pluginName"})
public class PluginSpec implements Comparable<PluginSpec> {
	private String pluginName;
	private String rootPackage;
	private Option<String> parentPlugin;

	/**
	 * Check whether the plugin is a base plugin or not.
	 * It boils down to whether it has a defined parent plugin.
	 * @return
	 */
	public boolean isBasePlugin(){
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
		  .map((name) -> name.replaceAll("\\.properties", ""))
		  .map((name) -> name.replaceAll("(.*)_.*$", "$1"));
	}
}
