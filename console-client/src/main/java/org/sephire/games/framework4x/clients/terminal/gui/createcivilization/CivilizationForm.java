package org.sephire.games.framework4x.clients.terminal.gui.createcivilization;

import io.vavr.control.Try;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.utils.I18NString;

import java.lang.reflect.Method;
import java.util.Locale;

@NoArgsConstructor
@Getter
@Setter
public class CivilizationForm {
	private String identifier;
	private String name;
	private String description;
	private String demonymSingular;
	private String demonymPlural;

	public static Try<Method> setterFor(String fieldName) {
		return Try.of(() -> CivilizationForm.class.getMethod("set" + capitalize(fieldName),String.class));
	}

	private static String capitalize(String string) {
		var upperCaseFirstLetter = ("" + string.charAt(0)).toUpperCase();

		return upperCaseFirstLetter + string.substring(1);
	}

	protected Civilization toCivilization() {
		return new Civilization(identifier,
		  I18NString.builder().withEntry(Locale.ENGLISH, name).build().get(),
		  I18NString.builder().withEntry(Locale.ENGLISH, description).build().get(),
		  I18NString.builder().withEntry(Locale.ENGLISH, demonymSingular).build().get(),
		  I18NString.builder().withEntry(Locale.ENGLISH, demonymPlural).build().get());
	}
}
