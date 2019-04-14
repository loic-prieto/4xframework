package org.sephire.games.framework4x.core.model.config.userpreferences;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.InvalidConfigurationObjectCast;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

/**
 * <p>Stores the user's preferences in a config-like container with its own set of keys</p>
 *
 * <p>As users may update their preferences at runtime, this class is mutable, unlike the general global
 * configuration object.</p>
 */
public class UserPreferences {

	public final static Path HOME_FOLDER = Path.of(System.getProperty("user.home"));
	public final static Path USER_PREFERENCES_FOLDER = HOME_FOLDER.resolve(".4xframework");

	private Map<UserPreferencesKeys, Object> preferences;

	public UserPreferences() {
		this.preferences = HashMap.empty();
	}

	public <T> Try<Option<T>> getUserPreference(UserPreferencesKeys key, Class<T> preferenceType) {
		return Try.of(() -> this.preferences.get(key).map(preferenceType::cast))
		  .mapFailure(
			Case($(instanceOf(ClassCastException.class)), (e) -> new InvalidConfigurationObjectCast(key, preferenceType))
		  );
	}

	public void updateUserPreference(UserPreferencesKeys key, Object value) {
		this.preferences = preferences.put(key, value);
	}

	/**
	 * <p>Save a string to a file inside the user preferences folder</p>
	 * <p>The encoding used is UTF-8</p>
	 * @param content
	 * @param filename
	 * @return
	 */
	public Try<Void> saveToFile(String content, Path filename) {
		return Try.of(() -> {
			var fullPath = USER_PREFERENCES_FOLDER.resolve(filename);

			var intermediaryFolders = fullPath.getParent();
			if (!Files.exists(intermediaryFolders)) {
				Files.createDirectories(intermediaryFolders);
			}

			Files.writeString(fullPath, content, StandardCharsets.UTF_8);

			return null;
		});
	}

	/**
	 * <p>Retrieves the string content of a file stored inside the user preferences folder</p>
	 * <p>Assumes the file is encoded in UTF-8</p>
	 * @param filename
	 * @return
	 */
	public Try<String> retrieveFile(Path filename) {
		return Try.of(() -> {
			var filepath = USER_PREFERENCES_FOLDER.resolve(filename);
			if (!Files.exists(filepath)) {
				throw new FileNotFoundException(filepath.toString());
			}

			return Files.readString(filepath, StandardCharsets.UTF_8);
		});
	}
}
