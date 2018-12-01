package org.sephire.games.framework4x.core.plugins.configuration;

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.classpath.ClasspathConfigurationSource;
import org.cfg4j.source.compose.FallbackConfigurationSource;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

/**
 * Utility class to return configuration objects (from CFG4J) from
 * yaml files stored in the classpath.
 */
public class ConfigLoader {

	/**
	 * Given a classpath file name, returns a cf4j configuration provider.
	 *
	 * May return:
	 *  - ConfigFileNotFoundException
	 *
	 * @param classpathFilename
	 * @return
	 */
	public static Try<CoreConfigProvider> getConfigFor(String classpathFilename) {

		return getClasspathFileURI(classpathFilename)
			.map(Paths::get)
			.map(Arrays::asList)
			.map((array) -> (ConfigFilesProvider) () -> array)
			.map(ConfigLoader::buildCompositeConfigurationSource)
			.map((source) -> new ConfigurationProviderBuilder().withConfigurationSource(source).build())
			.map(CoreConfigProvider::new);
	}

	/**
	 * Files can be both loaded from the classpath or the filesystem.
	 * This composite configuration source takes care of loading from both.
	 *
	 * @param filesProvider
	 * @return
	 */
	private static ConfigurationSource buildCompositeConfigurationSource(ConfigFilesProvider filesProvider){
		return new FallbackConfigurationSource(new FilesConfigurationSource(filesProvider),new ClasspathConfigurationSource(filesProvider));
	}

	private static Try<URI> getClasspathFileURI(String classpathFilename) {
		return Try.of(() -> Option.of(ClassLoader.getSystemClassLoader().getResource(classpathFilename))
			.getOrElseThrow(() -> new ConfigFileNotFoundException(classpathFilename)))
			.flatMap((url) -> Try.of(() -> url.toURI()))
			.mapFailure(Case($(instanceOf(URISyntaxException.class)), e -> new ConfigFileNotFoundException(classpathFilename)));
	}

}
