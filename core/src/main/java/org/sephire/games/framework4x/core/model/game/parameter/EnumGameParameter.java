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
package org.sephire.games.framework4x.core.model.game.parameter;

import io.vavr.collection.List;
import lombok.Getter;
import org.sephire.games.framework4x.core.model.game.GameParameter;

@Getter
public class EnumGameParameter<ENUM extends Enum & I18NEnum> extends GameParameter<ENUM> {

	private Class<ENUM> enumClass;

	public EnumGameParameter(String label, String description, String category, ENUM defaultValue,
							 List<ENUM> restrictionValues, boolean areMultipleChoicesAllowed,
							 String configurationKey,Class<ENUM> enumClass) {
		super(label, description, category, defaultValue, restrictionValues, areMultipleChoicesAllowed, configurationKey);
		this.enumClass = enumClass;
	}

	public static <T extends Enum & I18NEnum> Builder builder(){
		return new Builder<T>();
	}

	public static class Builder<ENUM extends Enum & I18NEnum> extends GameParameter.Builder<ENUM, EnumGameParameter.Builder,EnumGameParameter> {

		private Class<ENUM> enumClass;

		public Builder ofEnum(Class<ENUM> enumClass) {
			this.enumClass = enumClass;
			return this;
		}

		@Override
		public Builder limitedTo(List limitedValues) {
			throw new RuntimeException("This method cannot be called in an enum builder, use ofEnum instead");
		}

		@Override
		public EnumGameParameter<ENUM> build() {
			return new EnumGameParameter<>(
			  label,description,category,defaultValue,null,
			  areMultipleChoicesAllowed,configurationKey,enumClass
			);
		}
	}
}
