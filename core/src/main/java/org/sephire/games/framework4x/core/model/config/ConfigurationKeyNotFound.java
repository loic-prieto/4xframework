package org.sephire.games.framework4x.core.model.config;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

@Getter
public class ConfigurationKeyNotFound extends Framework4XException {

	private ConfigKeyEnum notFoundKey;

	public ConfigurationKeyNotFound(ConfigKeyEnum notFoundKey) {
		super("The key '"+notFoundKey+"' doesn't exist in the configuration global object");
		this.notFoundKey = notFoundKey;
	}
}
