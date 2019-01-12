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
package org.sephire.games.framework4x.core.model.game.parameter;

import io.vavr.collection.List;
import org.sephire.games.framework4x.core.model.game.GameParameter;

public class CustomGameParameter<CUSTOM_TYPE extends CustomGameParameterType>
  extends GameParameter<CUSTOM_TYPE> {

	public CustomGameParameter(String label, String description, String category, CUSTOM_TYPE defaultValue,
							   List<CUSTOM_TYPE> restrictionValues, boolean areMultipleChoicesAllowed, String configurationKey) {
		super(label, description, category, defaultValue, restrictionValues, areMultipleChoicesAllowed, configurationKey);
	}

	public static <T extends CustomGameParameterType> Builder builder() {
		return new Builder<T>();
	}

	public static class Builder<CUSTOM_TYPE extends CustomGameParameterType>
	  extends GameParameter.Builder<CUSTOM_TYPE, CustomGameParameter.Builder,CustomGameParameter<CUSTOM_TYPE>> {

		@Override
		public CustomGameParameter<CUSTOM_TYPE> build() {
			return new CustomGameParameter<>(label,description,category,defaultValue,
			  valuesLimitedTo,areMultipleChoicesAllowed,configurationKey);
		}
	}
}
