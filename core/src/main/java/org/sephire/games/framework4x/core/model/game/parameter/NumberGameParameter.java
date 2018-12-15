package org.sephire.games.framework4x.core.model.game.parameter;

import io.vavr.collection.List;
import org.sephire.games.framework4x.core.model.game.GameParameter;

public class NumberGameParameter extends GameParameter<Number> {
	public NumberGameParameter(String label, String description, String category,
							   Number defaultValue, List<Number> restrictionValues, boolean areMultipleChoicesAllowed,
							   String configurationKey) {
		super(label, description, category, defaultValue, restrictionValues, areMultipleChoicesAllowed, configurationKey);
	}

	public static Builder builder() {
		return new NumberGameParameter.Builder();
	}

	public static class Builder extends GameParameter.Builder<Number,NumberGameParameter.Builder,NumberGameParameter> {

		@Override
		public NumberGameParameter build() {
			return new NumberGameParameter(label,description,category,defaultValue,
			  valuesLimitedTo,areMultipleChoicesAllowed,configurationKey);
		}
	}
}
