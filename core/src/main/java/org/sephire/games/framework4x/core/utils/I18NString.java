package org.sephire.games.framework4x.core.utils;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.EqualsAndHashCode;

import java.util.Locale;

/**
 * Represents a String with a value for each defined locale.
 * The string is immutable.
 */
@EqualsAndHashCode
public class I18NString {
	private Map<Locale, String> values;

	private I18NString(Map<Locale, String> entries) {
		this.values = entries;
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Returns the value associated to the locale if it has been defined
	 * @param locale
	 * @return
	 */
	public Option<String> getFor(Locale locale) {
		return values.get(locale);
	}

	/**
	 * Returns a list of the entries in this string to ease automatic handling
	 * @return
	 */
	public List<Tuple2<Locale,String>> getEntries() {
		return values.iterator().collect(List.collector());
	}

	/**
	 * Return the first available value from the list of locale-value set of entries.
	 * For a I18NString to exist, at least an entry must exist, so this method is guaranteed to return something.
	 * @return
	 */
	public String getAvailableValue() {
		return values.values().get();
	}

	public static class Builder {
		private Map<Locale, String> entries;

		public Builder() {
			this.entries = HashMap.empty();
		}

		public Builder withEntry(Locale locale, String value) {
			this.entries = entries.put(locale, value);
			return this;
		}

		public Try<I18NString> build() {
			return Try.of(() -> {
				if (entries.isEmpty()) {
					throw new IllegalArgumentException("At least one entry must be present in the entry list for this localized string");
				}

				return new I18NString(entries);
			});
		}
	}
}
