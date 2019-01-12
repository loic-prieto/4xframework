package org.sephire.games.framework4x.clients.terminal.gui.components.map;

import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import io.vavr.control.Try;
import org.sephire.games.framework4x.clients.terminal.api.config.ConsoleClientConfigKeyEnum;
import org.sephire.games.framework4x.clients.terminal.api.config.TerrainsMapping;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.game.Game;
import org.sephire.games.framework4x.core.model.map.GameMap;

/**
 * A component that draws the current zone of the game map,
 * can be embedded in any container.
 */
public class MapComponent extends AbstractComponent<MapComponent> {

	private MapViewport viewport;
	private GameMap map;
	private TerrainsMapping mappings;

	private MapComponent(GameMap map,TerrainsMapping mappings) {
		this.viewport = new MapViewport();
		this.map = map;
		this.mappings = mappings;
	}

	public static Try<MapComponent> of(Game game) {
		return Try.of(()->{
			var configuration = game.getConfiguration();
			var mappings = configuration.getConfiguration(ConsoleClientConfigKeyEnum.TERRAIN_CHARACTER_MAPPING,TerrainsMapping.class);
			if(mappings.isFailure() || mappings.get().isEmpty()){
				throw new NoTerrainMappingFoundException();
			}

			return new MapComponent(game.getMap(),mappings.get().get());
		});

	}

	@Override
	protected ComponentRenderer<MapComponent> createDefaultRenderer() {
		return new MapComponentRenderer(viewport,map,mappings);
	}
}
