package org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.ConfigurationKeyNotFound;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.config.userpreferences.UserPreferences;
import org.sephire.games.framework4x.core.plugins.configuration.resources.ConfigResourceLoader;
import org.sephire.games.framework4x.core.plugins.configuration.resources.xml.civilizations.Civilizations;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.sephire.games.framework4x.core.plugins.configuration.resources.xml.Utils.Marshalling.getUnmarshaller;

/**
 * <p>Loads civilization config from the user preferences folder,
 * overwriting the already loaded plugins configuration</p>
 */
public class UserPreferencesCivilizationLoader implements ConfigResourceLoader {

	private static final Path CUSTOM_CIVILIZATIONS_FOLDER = Path.of("civilizations");

	@Override
	public Try<Void> load(Configuration.Builder configuration) {
		return Try.of(() -> {
			var userPreferences = new UserPreferences();
			var unmarshaller = getUnmarshaller().getOrElseThrow(t -> t);

			var customCivsFolder = UserPreferences.USER_PREFERENCES_FOLDER.resolve(CUSTOM_CIVILIZATIONS_FOLDER);
			if (Files.exists(customCivsFolder)) {

				var customCivs = Files.list(customCivsFolder)
				  .map(CUSTOM_CIVILIZATIONS_FOLDER::resolve)
				  .map(userPreferences::retrieveFile).filter(Try::isSuccess).map(Try::get)
				  .map(StringReader::new)
				  .map(s -> Try.of(() -> unmarshaller.unmarshal(s))).filter(Try::isSuccess).map(Try::get)
				  .map(c -> (Civilizations) c)
				  .map(CivilizationDomainMapper::mapFrom).filter(Try::isSuccess).map(Try::get)
				  .collect(List.collector()).flatMap(cs -> cs)
				  .map(civ -> Tuple.of(civ.getIdentifier(), civ))
				  .collect(HashMap.collector());

				var loadedCivilizations = configuration
				  .getConfig(CoreConfigKeyEnum.CIVILIZATIONS, Map.class).getOrElseThrow(t -> t)
				  .map(m -> (Map<String, Civilization>) m)
				  .getOrElseThrow(() -> new ConfigurationKeyNotFound(CoreConfigKeyEnum.CIVILIZATIONS));

				configuration.putConfig(CoreConfigKeyEnum.CIVILIZATIONS, customCivs.merge(loadedCivilizations));
			}

			return null;
		});
	}
}
