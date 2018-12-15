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
