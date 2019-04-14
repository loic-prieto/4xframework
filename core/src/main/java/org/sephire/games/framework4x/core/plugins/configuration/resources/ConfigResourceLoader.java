package org.sephire.games.framework4x.core.plugins.configuration.resources;

import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;

/**
 * <p>Represents a loader for the configuration provided by a plugin</p>
 * <p>A configuration may be obtained from different sources:<ul>
 *     <li>A programmatic configuration class a la Java Config</li>
 *     <li>An xml file in the classpath</li>
 *     <li>A database</li>
 *     <li>Event a webservice!</li>
 * </ul>
 * This component is used by the plugin load process to load configuration
 * </p>
 */
public interface ConfigResourceLoader {
	Try<Void> load(Configuration.Builder configuration);
}
