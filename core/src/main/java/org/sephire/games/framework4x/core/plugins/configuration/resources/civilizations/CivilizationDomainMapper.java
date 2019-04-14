package org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.plugins.configuration.resources.I18NDomainMapper;
import org.sephire.games.framework4x.core.plugins.configuration.resources.xml.civilizations.Civilizations;

import static org.sephire.games.framework4x.core.utils.FunctionalUtils.Collectors.toTry;

/**
 * <p>This class transforms a deserialized civilization config file into a list of
 * civilizations from the domain object.</p>
 * <p>This class is needed because we don't want to touch the automatically generated
 * xml dtos that jxc has produced from the schema file (and in that way to be able to easily
 * generate new ones when the schema changes</p>
 */
public class CivilizationDomainMapper {

	/**
	 * Maps the civilizations from the deserialized dto to the domain object
	 * @param deserializedCivilizations
	 * @return
	 */
	public static Try<List<Civilization>> mapFrom(Civilizations deserializedCivilizations) {
		return deserializedCivilizations.getCivilization().stream()
		  .map(CivilizationDomainMapper::modelFromXml)
		  .collect(toTry())
		  .map(s -> s.collect(List.collector()));
	}

	/**
	 * Maps from the domain object to the serializing dto
	 */
	public static Try<Civilizations> mapFrom(List<Civilization> civilizations) {
		return Try.of(()->{
			Civilizations result = new Civilizations();

			civilizations
			  .map(c-> Try.of(()->{
					var civilization = new org.sephire.games.framework4x.core.plugins.configuration.resources.xml.civilizations.Civilization();
					civilization.setIdentifier(c.getIdentifier());
					civilization.setName(I18NDomainMapper.mapFrom(c.getName()).getOrElseThrow(t->t));
					civilization.setDescription(I18NDomainMapper.mapFrom(c.getDescription()).getOrElseThrow(t->t));
					civilization.setDemonymSingular(I18NDomainMapper.mapFrom(c.getDemonymSingular()).getOrElseThrow(t->t));
					civilization.setDemonymPlural(I18NDomainMapper.mapFrom(c.getDemonymPlural()).getOrElseThrow(t->t));

					return civilization;
				}))
			  .collect(toTry()).getOrElseThrow(t->t)
			  .forEach(c->result.getCivilization().add(c));

			return result;
		});
	}

	private static Try<Civilization> modelFromXml(org.sephire.games.framework4x.core.plugins.configuration.resources.xml.civilizations.Civilization civilization) {
		return Try.of(() -> new Civilization(civilization.getIdentifier(),
		  I18NDomainMapper.mapFrom(civilization.getName()).getOrElseThrow(t -> t),
		  I18NDomainMapper.mapFrom(civilization.getDemonymSingular()).getOrElseThrow(t -> t),
		  I18NDomainMapper.mapFrom(civilization.getDemonymPlural()).getOrElseThrow(t -> t),
		  I18NDomainMapper.mapFrom(civilization.getDescription()).getOrElseThrow(t -> t)));
	}
}
