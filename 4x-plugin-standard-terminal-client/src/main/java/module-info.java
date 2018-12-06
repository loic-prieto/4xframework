module org.sephire.games.framework4x.plugins.standard.terminal {
	exports org.sephire.games.framework4x.plugins.standard.terminal;

	opens org.sephire.games.framework4x.plugins.standard.terminal to org.sephire.games.framework4x.core;

	requires transitive org.sephire.games.framework4x.plugins.standard;
	requires transitive org.sephire.games.framework4x.clients.terminal.api;

	requires lombok;
	requires io.vavr;
}