package org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.civilization.Civilization;

import java.io.StringWriter;

import static org.sephire.games.framework4x.core.plugins.configuration.resources.xml.Utils.Marshalling.getMarshaller;

public class CivilizationsSerializer {

	/**
	 * Given a list of civilizations, serializes them to XML into a string
	 * @param civilizations
	 * @return
	 */
	public static Try<String> toXmlString(List<Civilization> civilizations) {
		return Try.of(() -> {
			var mappedCivilizations = CivilizationDomainMapper.mapFrom(civilizations).getOrElseThrow(t -> t);

			StringWriter writer = new StringWriter();

			getMarshaller().getOrElseThrow(t -> t)
			  .marshal(mappedCivilizations, writer);

			return writer.toString();
		});
	}


}
