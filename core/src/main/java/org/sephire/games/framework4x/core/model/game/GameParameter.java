/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.core.model.game;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Parameter to be defined when creating a game.
 * For example: difficulty level, or selected map, or technology research rate.
 *
 * Each plugin will define what game parameters are available when creating a game.
 * And each plugin will receive the values given by the user on a game initialization hook.
 *
 * There are different game parameter types that can be used to ease the building of game parameters
 * from a plugin.
 *
 * Example:
 * <pre>
 *     @Override
 *     List<GameParameter> buildGameParameters() {
 *       return List.of(
 *           new StringGameParameter.Builder()
 *             .withLabel("18n.param.name")
 *             .withDescription("i18n.param.description")
 *             .withDefaultValue("default")
 *             .underConfigKey("plugin1.gameparams.param1")
 *             .limitedTo(List.of("value1","value2","value3")
 *             .build()
 *       );
 *     }
 * </pre>
 */
@AllArgsConstructor
@Getter
public abstract class GameParameter<TYPE> {
	/**
	 * i18n resource name for the label to show
	 */
	private String label;
	/**
	 * i18n resource name for the description of the parameter
	 */
	private String description;

	/**
	 * An option may be grouped into a category, which is a hint for the client to show same-category
	 * options together.
	 */
	private String category;

	/**
	 * A parameter must have a default value to reduce configuration friction
	 * for the player.
	 */
	private TYPE defaultValue;

	/**
	 * A parameter may be restricted to just some values.
	 * This is the attribute that holds those restricted values.
	 */
	private List<TYPE> restrictionValues;

	/**
	 * A parameter may be a list of values, instead of a single discrete value.
	 */
	private boolean areMultipleChoicesAllowed;

	/**
	 * The string key under which this game parameter will be stored in the
	 * game configuration object.
	 */
	private String configurationKey;

	public static abstract class Builder<VALUE_TYPE,BUILDER extends Builder, PARAMETER_CLASS> {

		protected String label;
		protected String description;
		protected String category = "default";
		protected VALUE_TYPE defaultValue;
		protected List<VALUE_TYPE> valuesLimitedTo = List.empty();
		protected boolean areMultipleChoicesAllowed = false;
		protected String configurationKey;

		public BUILDER withLabel(String label) {
			this.label = label;
			return (BUILDER)this;
		}

		public BUILDER withDescription(String description) {
			this.description = description;
			return (BUILDER)this;
		}

		public BUILDER belongsToCategory(String category) {
			this.category = category;
			return (BUILDER)this;
		}

		public BUILDER withDefaultValue(VALUE_TYPE defaultValue) {
			this.defaultValue = defaultValue;
			return (BUILDER)this;
		}

		public BUILDER limitedTo(List<VALUE_TYPE> limitedValues) {
			this.valuesLimitedTo = limitedValues;
			return (BUILDER)this;
		}

		public BUILDER enableMultipleChoices() {
			this.areMultipleChoicesAllowed = true;
			return (BUILDER)this;
		}

		public BUILDER underConfigKey(String key) {
			this.configurationKey = key;
			return (BUILDER)this;
		}

		public abstract PARAMETER_CLASS build();

	}

}
