package org.sephire.games.framework4x.clients.terminal.gui.startgame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.createcivilization.CreateCivilizationWindow;
import org.sephire.games.framework4x.clients.terminal.gui.createcivilization.NewCivilizationAddedEvent;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.TranslationNotFoundException;
import org.sephire.games.framework4x.clients.terminal.utils.ToStringDecorator;
import org.sephire.games.framework4x.core.model.civilization.Civilization;
import org.sephire.games.framework4x.core.model.config.Configuration;
import org.sephire.games.framework4x.core.model.config.ConfigurationKeyNotFound;
import org.sephire.games.framework4x.core.model.config.CoreConfigKeyEnum;
import org.sephire.games.framework4x.core.model.config.userpreferences.UserPreferences;
import org.sephire.games.framework4x.core.plugins.configuration.resources.civilizations.CivilizationsSerializer;

import java.nio.file.Path;
import java.util.Locale;

import static java.lang.String.format;
import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Translation.getTranslationFor;
import static org.sephire.games.framework4x.core.utils.Validations.areArgumentsNotNull;

/**
 * <p>A panel to show a list of civilization that allows to pick one. Each civilization has a description that is shown
 * when selected.</p>
 */
@Slf4j
public class SelectCivilizationPanel extends Panel {

	private Label descriptionLabel;
	private ComboBox<ToStringDecorator<Civilization>> civilizationComboBox;

	private SelectCivilizationPanel(Configuration.Builder configuration, Basic4XWindow parent) throws Throwable {
		setLayoutManager(new LinearLayout(Direction.VERTICAL));

		var inlinePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));

		inlinePanel.addComponent(new Label(getTranslationFor(Locale.ENGLISH, "startGameWindow.civsPanel.header.label")
		  .getOrElseThrow(()->new TranslationNotFoundException("startGameWindow.civsPanel.header.label"))));

		this.civilizationComboBox = buildCivSelectBox(configuration, parent).getOrElseThrow(t -> t);
		inlinePanel.addComponent(civilizationComboBox);
		addComponent(inlinePanel);

		this.descriptionLabel = buildDescriptionLabel().getOrElseThrow(t -> t);
		addComponent(descriptionLabel.withBorder(Borders.singleLine(
		  getTranslationFor(Locale.ENGLISH, "startGameWindow.civsPanel.description.label")
			.getOrElseThrow(() -> new TranslationNotFoundException("startGameWindow.civsPanel.description.label")))));

		addComponent(buildCreateCivilizationButton(parent).getOrElseThrow(t -> t));

		parent.registerEventListener(NewCivilizationAddedEvent.class, (event) -> {
			var civilization = event.getCivilization();

			civilizationComboBox.addItem(wrapCivilization(civilization));

			var serializedCivilization = CivilizationsSerializer.toXmlString(List.of(civilization));
			if(serializedCivilization.isFailure()) {
				var errorMessage = getTranslationFor(Locale.ENGLISH,"startGameWindow.civsPanel.saveCiv.error")
				  .getOrElseThrow(()->new TranslationNotFoundException("startGameWindow.civsPanel.saveCiv.error"));
				MessageDialog.showMessageDialog(parent.getTextGUI(), "Error", errorMessage, MessageDialogButton.OK);
				log.error(format("Could not deserialize civilizations: %s",serializedCivilization.getCause().getMessage()));
			} else {
				var userPreferences = configuration.getUserPreferences();

				var saveResult = userPreferences.saveToFile(
				  serializedCivilization.get(),
				  Path.of("civilizations",civilization.getIdentifier().concat(".xml")));
				if(saveResult.isFailure()) {
					var errorMessage = getTranslationFor(Locale.ENGLISH,"startGameWindow.civsPanel.saveCiv.error")
					  .getOrElseThrow(()->new TranslationNotFoundException("startGameWindow.civsPanel.saveCiv.error"));
					MessageDialog.showMessageDialog(parent.getTextGUI(), "Error", errorMessage, MessageDialogButton.OK);
					log.error(format("Could not save the civilizations: %s",saveResult.getCause().getMessage()));
				}
			}

		});
	}

	private Try<ComboBox> buildCivSelectBox(Configuration.Builder configuration, Basic4XWindow parent) {
		return Try.of(() -> {
			var civSelect = new ComboBox<ToStringDecorator<Civilization>>();
			configuration.getConfig(CoreConfigKeyEnum.CIVILIZATIONS, Map.class)
			  .getOrElseThrow(t -> t)
			  .map(civs -> (Map<String, Civilization>) civs)
			  .getOrElseThrow(() -> new ConfigurationKeyNotFound(CoreConfigKeyEnum.CIVILIZATIONS))
			  .map(Tuple2::_2)
			  .map(civ -> new ToStringDecorator<>(civ, (c -> c.getName()
				.getFor(Locale.ENGLISH).getOrElse(c.getName().getAvailableValue()))))
			  .forEach(civSelect::addItem);
			civSelect.addListener((selectedIndex, previousSelection) -> {
				var selectedCivilization = civSelect.getItem(selectedIndex).getWrappedObject();
				updateCivilizationDescription(selectedCivilization);
				parent.fireEvent(new CivilizationSelectedEvent(selectedCivilization));
			});

			return civSelect;
		});
	}

	private static ToStringDecorator<Civilization> wrapCivilization(Civilization civilization) {
		return new ToStringDecorator<>(civilization, (c -> c.getName()
		  .getFor(Locale.ENGLISH).getOrElse(c.getName().getAvailableValue())));
	}

	private Try<Label> buildDescriptionLabel() {
		return Try.of(() -> {
			var currentCivilization = civilizationComboBox.getItem(0).getWrappedObject();

			return new Label(currentCivilization.getDescription()
			  .getFor(Locale.ENGLISH).getOrElse(currentCivilization.getDescription().getAvailableValue()));
		});
	}

	private Try<Button> buildCreateCivilizationButton(Basic4XWindow parent) {
		return Try.of(() -> {
			var createButton = new Button(getTranslationFor(Locale.ENGLISH, "startGameWindow.civsPanel.createCiv.label")
			  .getOrElseThrow(() -> new TranslationNotFoundException("startGameWindow.civsPanel.createCiv.label")));

			createButton.addListener((event) -> {
				var createCivilizationWindow = CreateCivilizationWindow.builder()
				  .withTextGUI(parent.getOverridenTextGui())
				  .withEventBus(parent.getEventBus())
				  .build();

				if (createCivilizationWindow.isFailure()) {
					var errorMessage = getTranslationFor(Locale.ENGLISH, "startGameWindow.civsPanel.createCiv.windowerror")
					  .getOrElseThrow(() -> new TranslationNotFoundException("startGameWindow.civsPanel.createCiv.windowerror"));
					MessageDialog.showMessageDialog(parent.getTextGUI(), "Error", errorMessage, MessageDialogButton.OK);
					log.error(format("The create civilization window could not be created: %s", createCivilizationWindow.getCause().getMessage()));
					return;
				}

				parent.getOverridenTextGui().addWindow(createCivilizationWindow.get());
				parent.getOverridenTextGui().setActiveWindow(createCivilizationWindow.get());
			});

			return createButton;
		});
	}

	private void updateCivilizationDescription(Civilization civilization) {
		var descriptionLabel = civilization.getDescription()
		  .getFor(Locale.ENGLISH).getOrElse(civilization.getDescription().getAvailableValue());

		this.descriptionLabel.setText(descriptionLabel);
	}

	public static BuilderConfiguration builder() {
		return new Builder();
	}

	interface BuilderConfiguration {
		BuilderParent withConfiguration(Configuration.Builder configuration);
	}

	public static class Builder implements BuilderConfiguration, BuilderParent, BuilderBuilder {

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

				return new SelectCivilizationPanel(configuration,parent)
				  .withBorder(
					Borders.singleLine(
					  getTranslationFor(Locale.ENGLISH,"startGameWindow.civsPanel.title")
						.getOrElseThrow(()->new TranslationNotFoundException("startGameWindow.civsPanel.title"))));
			});
		}
	}
	interface BuilderParent { BuilderBuilder withParent(Basic4XWindow parent);}
	interface BuilderBuilder { Try<Border> build();}
}
