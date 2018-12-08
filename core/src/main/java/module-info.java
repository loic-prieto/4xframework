module org.sephire.games.framework4x.core {
	exports org.sephire.games.framework4x.core;
	exports org.sephire.games.framework4x.core.plugins;
	exports org.sephire.games.framework4x.core.plugins.configuration;
	exports org.sephire.games.framework4x.core.model.map;
	exports org.sephire.games.framework4x.core.model.ai;
	exports org.sephire.games.framework4x.core.model.config;
	exports org.sephire.games.framework4x.core.model.gameplay;
	exports org.sephire.games.framework4x.core.model.research;
	exports org.sephire.games.framework4x.core.utils;

	requires io.vavr;
	requires static lombok;
	requires yacl4j.core;
	requires org.slf4j;
}