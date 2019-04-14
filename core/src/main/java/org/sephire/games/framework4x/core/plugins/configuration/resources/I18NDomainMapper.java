package org.sephire.games.framework4x.core.plugins.configuration.resources;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.plugins.configuration.resources.xml.common.LocaleEntry;
import org.sephire.games.framework4x.core.utils.I18NString;

import java.util.Locale;

/**
 * <p>This mapper exists to convert back and forth between serialized I18N strings in config files to I18N strings
 * in the domain space.</p>
 */
public class I18NDomainMapper {

	/**
	 * Map from the deserialized format to the domain model
	 * @param xmlString
	 * @return
	 */
	public static Try<I18NString> mapFrom(
	  org.sephire.games.framework4x.core.plugins.configuration.resources.xml.common.I18NString xmlString) {
		return Try.of(()->{
			var i18nBuilder = I18NString.builder();

			xmlString.getI18NString().stream()
			  .forEach((entry)->i18nBuilder.withEntry(Locale.forLanguageTag(entry.getLanguage()),entry.getValue()));

			return i18nBuilder.build().getOrElseThrow(t->t);
		});
	}

	/**
	 * Maps from the domain object to the serializing dto
	 * @param i18NString
	 * @return
	 */
	public static Try<org.sephire.games.framework4x.core.plugins.configuration.resources.xml.common.I18NString> mapFrom(
	  I18NString i18NString) {
		return Try.of(()->{
			var xmlString = new org.sephire.games.framework4x.core.plugins.configuration.resources.xml.common.I18NString();
			i18NString.getEntries().map(entry->{
				var xmlEntry = new LocaleEntry();
				xmlEntry.setLanguage(entry._1.toLanguageTag());
				xmlEntry.setValue(entry._2);
				return xmlEntry;
			}).forEach(entry->xmlString.getI18NString().add(entry));

			return xmlString;
		});
	}
}
