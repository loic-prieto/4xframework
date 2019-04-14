package org.sephire.games.framework4x.clients.terminal.utils;

import io.vavr.control.Option;

import java.util.Locale;

/**
 * <p>Fetches translations from the UI translation bundle of the console client.</p>
 * <p>This is in opposition to the configuration translation service, which retrieves the translations from
 * the plugins loaded configuration.</p>
 */
public class UITranslationService {

	/**
	 * Given a label key, retrieves the translation for a given locale applying the given parameters.
	 * @param locale
	 * @param labelKey
	 * @param params
	 * @return
	 */
	public Option<String> getTranslationFor(Locale locale, String labelKey, Object... params) {
		return Terminal.Translation.getTranslationFor(locale,labelKey,params);
	}
}
