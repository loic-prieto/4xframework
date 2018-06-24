package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import lombok.Getter;
import lombok.NonNull;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;

public abstract class BaseLayout implements Layout {
	@NonNull
	@Getter
	private Container container;

	public BaseLayout(Container container) {
		this.container = container;
		container.setLayout(this);
	}
}
