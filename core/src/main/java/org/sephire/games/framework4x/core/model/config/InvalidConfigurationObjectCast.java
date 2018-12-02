package org.sephire.games.framework4x.core.model.config;

import lombok.Getter;
import org.sephire.games.framework4x.core.Framework4XException;

import static java.lang.String.format;

/**
 * This exception is thrown when trying to cast a configuration value into a type
 * that is not valid.
 * For example, trying to cast an integer type to String.
 *
 */
public class InvalidConfigurationObjectCast extends Framework4XException {
	@Getter
	private ConfigKeyEnum configKey;
	@Getter
	private Class<?> failingClassCast;

	public InvalidConfigurationObjectCast(ConfigKeyEnum configKey,Class<?> castTo) {
		super(format("Error casting config %s to class %s",configKey,castTo.getName()));
		this.configKey = configKey;
		this.failingClassCast = castTo;
	}
}
