package org.sephire.games.framework4x.core.model.civilization;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>The top entity to represent the civilization that the player is using in a game.</p>
 * <p>It holds all the current information about a civilization, like current resources, research techs discovered
 * and being discovered, armies, cities, etc.</p>
 * <p>This class is to be augmented when implementing each of these concepts.</p>
 */
@Getter
@AllArgsConstructor
public class Civilization {
	private String name;
	private String demonymSingular;
	private String demonymPlural;
	private String description;


}
