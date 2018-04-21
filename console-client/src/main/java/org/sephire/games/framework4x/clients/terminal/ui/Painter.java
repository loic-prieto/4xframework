package org.sephire.games.framework4x.clients.terminal.ui;

import com.googlecode.lanterna.screen.Screen;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * An abstraction of the terminal painter. Allows to easily draw character and widgets with the appropriate
 * transformations.
 */
@RequiredArgsConstructor
public class Painter {
	@NonNull
	@Getter
	private Screen screen;
	@NonNull
	@Getter
	private Viewport viewport;

}
