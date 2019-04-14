package org.sephire.games.framework4x.core.model.civilization;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sephire.games.framework4x.core.utils.I18NString;

/**
 * <p>The top entity to represent the civilization that the player is using in a game.</p>
 * <p>It holds all the current information about a civilization, like current resources, research techs discovered
 * and being discovered, armies, cities, etc.</p>
 * <p>This class is to be augmented when implementing each of these concepts.</p>
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Civilization {
	private String identifier;
	private I18NString name;
	private I18NString demonymSingular;
	private I18NString demonymPlural;
	private I18NString description;

}
