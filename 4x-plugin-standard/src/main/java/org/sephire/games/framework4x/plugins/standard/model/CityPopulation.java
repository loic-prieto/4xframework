package org.sephire.games.framework4x.plugins.standard.model;

import lombok.Getter;

/**
 * This class models the population inside a city. It contains just the data, since the behaviour will be defined
 * by both the standard plugin and dependent plugins.
 */
@Getter
public class CityPopulation {
	private long count;
}
