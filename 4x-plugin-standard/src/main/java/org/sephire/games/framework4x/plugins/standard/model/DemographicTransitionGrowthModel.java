package org.sephire.games.framework4x.plugins.standard.model;

/**
 * <p>The standard population growth model is based on the demographic transition theory</p>
 * <p>Take a look into the population-growth.md file to check how the population growth is modelled</p>
 * <p>The time unit is the year</p>
 */
public class DemographicTransitionGrowthModel implements PopulationGrowthModel {

	@Override
	public long calculatePopulationCount(CityPopulation cityPopulation, int timeDelta) {
		return 1000 * timeDelta;
	}
}
