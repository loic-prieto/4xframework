package org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations;


import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.plugins.CoreResourcesTypes;
import org.sephire.games.framework4x.core.plugins.configuration.InvalidConfigFileException;
import org.sephire.games.framework4x.core.plugins.configuration.resources.ConfigResourceLoader;
import org.sephire.games.framework4x.core.plugins.configuration.resources.xml.civilizations.Civilizations;

import javax.xml.bind.JAXBException;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations.CivilizationDomainMapper.mapFrom;
import static org.sephire.games.framework4x.core.plugins.configuration.resources.xml.Utils.Marshalling.getUnmarshaller;
import static org.sephire.games.framework4x.core.utils.ResourceLoading.fileDoesExist;
import static org.sephire.games.framework4x.core.utils.ResourceLoading.packageToFolderPath;

public class CivilizationsConfigurationLoader implements ConfigResourceLoader {
	private String pluginRootPackage;

	public CivilizationsConfigurationLoader(String pluginRootPackage) {
		this.pluginRootPackage = pluginRootPackage;
	}

	@Override
	public Try<Void> load(Configuration.Builder configuration) {
		return Try.of(() -> {
			String civilizationsFilename = packageToFolderPath(pluginRootPackage)
			  + "/"
			  + CoreResourcesTypes.CIVILIZATIONS.getFileName();

			if (fileDoesExist(civilizationsFilename)) {
				List<Civilization> civilizations = getCivilizationsFromConfig(civilizationsFilename)
				  .getOrElseThrow(t -> t);

				var civilizationsConfig = civilizations.map(c -> Tuple.of(c.getIdentifier(), c))
				  .collect(HashMap.collector());

				configuration.putConfig(CoreConfigKeyEnum.CIVILIZATIONS, civilizationsConfig);
			}
			return null;
		});

	}

	private Try<List<Civilization>> getCivilizationsFromConfig(String configfileName) {
		return Try.of(() -> {
			var civilizationsTestFile = getClass().getClassLoader().getResourceAsStream(configfileName);

			Civilizations xmlCivilizations = (Civilizations) getUnmarshaller().getOrElseThrow(t -> t)
			  .unmarshal(civilizationsTestFile);

			return mapFrom(xmlCivilizations).getOrElseThrow(t -> t);

		}).mapFailure(
		  Case($(instanceOf(JAXBException.class)), t -> new InvalidConfigFileException(configfileName, t))
		);
	}

}
