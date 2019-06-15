package org.sephire.games.framework4x.plugins.standard.model;

import lombok.Getter;
import org.sephire.games.framework4x.core.model.civilization.Civilization;

@Getter
public class City {
	private String name;
	private Civilization owner;
	private CityPopulation cityPopulation;

}
