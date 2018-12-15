package org.sephire.games.framework4x.core.model.game.parameter;

import io.vavr.collection.List;
import org.sephire.games.framework4x.core.model.game.GameParameter;

public class StringGameParameter extends GameParameter<String> {

	public StringGameParameter(String label, String description, String category, String defaultValue,
							   List<String> restrictionValues, boolean areMultipleChoicesAllowed,
							   String configurationKey) {
		super(label, description, category, defaultValue, restrictionValues, areMultipleChoicesAllowed, configurationKey);
	}

	public static Builder builder() {
		return new StringGameParameter.Builder();
	}

	public static class Builder extends GameParameter.Builder<String,StringGameParameter.Builder,StringGameParameter> {

		@Override
		public StringGameParameter build() {
			return new StringGameParameter(label,description,category,defaultValue,
			  valuesLimitedTo,areMultipleChoicesAllowed,configurationKey);
		}
	}
}
