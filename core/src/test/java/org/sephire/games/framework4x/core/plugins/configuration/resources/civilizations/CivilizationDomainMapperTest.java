package org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.plugins.configuration.resources.xml.civilizations.Civilizations;
import org.sephire.games.framework4x.core.utils.I18NString;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sephire.games.framework4x.core.plugins.configuration.resources.xml.Utils.Marshalling.getUnmarshaller;

public class CivilizationDomainMapperTest {

	private final static String CIVILIZATIONS_CONFIG_FILE = "org/sephire/games/framework4x/testing/testPlugin1/civilizations.xml";

	@Test
	@DisplayName("Given a deserialized civilizations dto, it should map correctly to the domain objects")
	public void should_map_correctly_to_a_domain_object() {

		var deserializedCivilizations = generateDeserializedCivilizations();
		assertTrue(deserializedCivilizations.isSuccess());

		var actualCivilizationsMapping = CivilizationDomainMapper.mapFrom(deserializedCivilizations.get());
		assertTrue(actualCivilizationsMapping.isSuccess());

		assertEquals(expectedCivilizations(), actualCivilizationsMapping.get());

	}

	private Try<Civilizations> generateDeserializedCivilizations() {
		return Try.of(() -> {
			var civilizationsTestFile = this.getClass().getClassLoader().getResourceAsStream(CIVILIZATIONS_CONFIG_FILE);
			if (civilizationsTestFile == null) {
				throw new IllegalArgumentException("The test civilizations file has not been found");
			}

			return (Civilizations) getUnmarshaller().getOrElseThrow(t->t)
			  .unmarshal(civilizationsTestFile);
		});
	}

	private List<Civilization> expectedCivilizations() {
		return List.of(new Civilization("test.civilization1",
		  org.sephire.games.framework4x.core.utils.I18NString.builder()
			.withEntry(Locale.ENGLISH, "Civilization 1")
			.withEntry(new Locale("es"), "Civilización 1")
			.build().get(),
		  org.sephire.games.framework4x.core.utils.I18NString.builder()
			.withEntry(Locale.ENGLISH, "tester")
			.withEntry(new Locale("es"), "testeador")
			.build().get(),
		  I18NString.builder()
			.withEntry(Locale.ENGLISH, "testers")
			.withEntry(new Locale("es"), "testeadores")
			.build().get(),
		  org.sephire.games.framework4x.core.utils.I18NString.builder()
			.withEntry(Locale.ENGLISH, "A test civilization")
			.withEntry(new Locale("es"), "Una civilización de test")
			.build().get()
		));
	}
}
