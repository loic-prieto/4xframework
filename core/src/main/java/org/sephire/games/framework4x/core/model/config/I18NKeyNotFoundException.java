package org.sephire.games.framework4x.core.model.config;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

import static java.lang.String.format;

/**
 * This exception is thrown when searching for a i18n resource by key, and the resource
 * is not found, and this is an application error.
 */
public class I18NKeyNotFoundException extends Framework4XException {
	@Getter
	private String i18nKey;

	public I18NKeyNotFoundException(String i18nKey) {
		super(format("The key %s was not found when searching for an i18n resource",i18nKey));
		this.i18nKey = i18nKey;
	}
}
