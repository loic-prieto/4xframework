package org.sephire.games.framework4x.core.model.map.items;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sephire.games.framework4x.core.model.map.Item;
import org.sephire.games.framework4x.core.model.map.Location;

@RequiredArgsConstructor
public abstract class BaseItem implements Item {
	@NonNull
	@Getter
	private Location location;

	public BaseItem() {
	}
}
