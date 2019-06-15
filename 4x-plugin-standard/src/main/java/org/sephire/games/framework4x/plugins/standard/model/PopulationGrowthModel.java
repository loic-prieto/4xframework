package org.sephire.games.framework4x.plugins.standard.model;

/**
 * <p>This is the interface that has to be implemented by all plugins providing population growth modelling
 * from scratch.</p>
 * <p>To add some percentages or numbers, or operate based on an existing population growth model, you can instead
 * use PopulationGrowthModelInterceptors</p>
 * <p>Which population growth model to use is defined in the configuration of plugins</p>
 */
public interface PopulationGrowthModel {

	/**
	 * Given a city population and a time delta, calculate the next variation of population.
	 * @param cityPopulation the population to use to calculate the population delta
	 * @param timeDelta The time unit depends on the population growth model used.
	 * @return
	 */
	long calculatePopulationCount(CityPopulation cityPopulation, int timeDelta);
}
