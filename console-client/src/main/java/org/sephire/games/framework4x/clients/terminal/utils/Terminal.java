package org.sephire.games.framework4x.clients.terminal.utils;

import com.googlecode.lanterna.TerminalSize;
import org.sephire.games.framework4x.core.model.map.Size;

public class Terminal {

	public static Size sizeFromTerminalSize(TerminalSize ts) {
		return new Size(ts.getColumns(),ts.getRows());
	}

}
