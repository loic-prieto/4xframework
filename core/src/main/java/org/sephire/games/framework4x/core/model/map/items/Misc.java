package org.sephire.games.framework4x.core.model.map.items;


import lombok.NonNull;
import lombok.Value;

@Value
public class Misc extends BaseItem {
	@NonNull
	private MiscType type;
}
