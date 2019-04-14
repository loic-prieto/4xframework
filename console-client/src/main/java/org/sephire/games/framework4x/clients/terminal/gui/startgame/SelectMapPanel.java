package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import com.googlecode.lanterna.gui2.*;
import io.vavr.collection.Set;
import io.vavr.control.Try;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.TranslationNotFoundException;
import org.sephire.games.framework4x.clients.terminal.utils.ToStringDecorator;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.plugins.map.MapGeneratorWrapper;

import java.util.Locale;

import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Translation.getTranslationFor;
import static org.sephire.games.framework4x.core.utils.Validations.areArgumentsNotNull;

/**
 * This panel holds the selection of a map in the start game screen.
 */
public class SelectMapPanel extends Panel {

	private Configuration.Builder configuration;

	private SelectMapPanel(Configuration.Builder configuration, Basic4XWindow parent) throws Throwable {
		this.configuration = configuration;

		setLayoutManager(new LinearLayout(Direction.VERTICAL));

		var mapGenerators = configuration.getConfig(CoreConfigKeyEnum.MAPS, Set.class).getOrElseThrow(t -> t)
		  .map((generators) -> (Set<MapGeneratorWrapper>) generators)
		  .getOrElseThrow(NoMapFoundsException::new);

		var inlinePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));

		inlinePanel.addComponent(new Label(
		  getTranslationFor(Locale.ENGLISH,"startGameWindow.optionsPane.map.label")
			.getOrElseThrow(()->new TranslationNotFoundException("startGameWindow.optionsPane.map.label"))));

		var mapSelection = new ComboBox<ToStringDecorator<MapGeneratorWrapper>>();
		mapGenerators
		  .map(this::stringifyMapGenerator)
		  .forEach(mapSelection::addItem);
		mapSelection.addListener((selectedIndex, previousSelection) -> {
			parent.fireEvent(new MapGeneratorSelectedEvent(mapSelection.getItem(selectedIndex).getWrappedObject()));
		});
		inlinePanel.addComponent(mapSelection);

		addComponent(inlinePanel);
	}

	private ToStringDecorator<MapGeneratorWrapper> stringifyMapGenerator(MapGeneratorWrapper mapGenerator) {
		return new ToStringDecorator<>(mapGenerator, (m) ->
		  configuration.getTranslationFor(Locale.ENGLISH, m.getDisplayKey())
			.getOrElse(getTranslationFor(Locale.ENGLISH,"startGameWindow.mapGeneratorWithInvalidI18N.title")
			  .getOrElseThrow(()->new TranslationNotFoundException("startGameWindow.mapGeneratorWithInvalidI18N.title"))));
	}

	public static BuilderConfiguration builder() {
		return new Builder();
	}

	interface BuilderConfiguration {
		BuilderParent withConfiguration(Configuration.Builder configuration);
	}

	public static class Builder implements BuilderConfiguration,BuilderParent,BuilderBuilder{

		private Configuration.Builder configuration;
		private Basic4XWindow parent;

		@Override
		public BuilderParent withConfiguration(Configuration.Builder configuration) {
			this.configuration = configuration;
			return this;
		}

		@Override
		public BuilderBuilder withParent(Basic4XWindow parent) {
			this.parent = parent;
			return this;
		}

		@Override
		public Try<Border> build() {
			return Try.of(()->{
				areArgumentsNotNull(configuration,parent).getOrElseThrow(t->t);

				return new SelectMapPanel(configuration,parent)
				  .withBorder(
					Borders.singleLine(
					  getTranslationFor(Locale.ENGLISH,"startGameWindow.mapPanel.title")
						.getOrElseThrow(()->new TranslationNotFoundException("startGameWindow.mapPanel.title"))));
			});
		}
	}
	interface BuilderParent { BuilderBuilder withParent(Basic4XWindow parent);}
	interface BuilderBuilder { Try<Border> build();}
}
