/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
