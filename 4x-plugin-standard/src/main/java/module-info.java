module org.sephire.games.framework4x.plugins.standard {
	exports org.sephire.games.framework4x.plugins.standard;

	opens org.sephire.games.framework4x.plugins.standard to org.sephire.games.framework4x.core;

	requires transitive org.sephire.games.framework4x.core;
	requires io.vavr;
	requires lombok;
	requires slf4j.api;
}