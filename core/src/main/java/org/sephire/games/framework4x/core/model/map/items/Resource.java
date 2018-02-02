package org.sephire.games.framework4x.core.model.map.items;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import org.sephire.games.framework4x.core.model.map.Item;

@Value
public class Resource implements Item {
	@NonNull
	@Getter
	private ResourceType resourceType;
}
