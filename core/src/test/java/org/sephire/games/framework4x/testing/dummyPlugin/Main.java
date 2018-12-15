package org.sephire.games.framework4x.testing.dummyPlugin;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.game.GameParameter;
import org.sephire.games.framework4x.core.model.game.parameter.NumberGameParameter;
import org.sephire.games.framework4x.core.model.game.parameter.StringGameParameter;
import org.sephire.games.framework4x.core.plugins.PluginInitializer;
import org.sephire.games.framework4x.core.plugins.configuration.PluginConfiguration;

import static io.vavr.control.Try.success;
import static org.sephire.games.framework4x.testing.dummyPlugin.DummyPluginConfigKeyEnum.KEY1;

/**
 * The main plugin class for the Dummy test plugin
 */
public class Main implements PluginInitializer, PluginConfiguration {

	@Override
	public Try<Void> pluginLoad(Configuration.Builder configuration) {
		configuration.addConfig(KEY1, "test");
		return success(null);
	}

	@Override
	public void pluginLoading(Configuration.Builder configuration) {

	}

	@Override
	public void gameLoading(Configuration.Builder configuration) {

	}

	@Override
	public List<? extends GameParameter> buildGameParameters() {
		return List.of(
		  StringGameParameter.builder()
			.withLabel("test.label")
			.withDescription("test.description")
			.limitedTo(List.of("value1", "value2"))
			.build(),
		  NumberGameParameter.builder()
			.withLabel("test.label2")
			.withDescription("test.description2")
			.limitedTo(List.range(1,10).map(Number.class::cast))
			.build()
		);
	}
}
