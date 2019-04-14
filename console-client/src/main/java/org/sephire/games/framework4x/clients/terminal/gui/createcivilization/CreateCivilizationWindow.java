package org.sephire.games.framework4x.clients.terminal.gui.createcivilization;

import com.googlecode.lanterna.gui2.*;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.sephire.games.framework4x.clients.terminal.api.events.WindowEventBus;
import org.sephire.games.framework4x.clients.terminal.gui.Basic4XWindow;
import org.sephire.games.framework4x.clients.terminal.gui.components.EventedTextBox;
import org.sephire.games.framework4x.clients.terminal.gui.gamewindow.TranslationNotFoundException;

import java.util.Locale;

import static org.sephire.games.framework4x.clients.terminal.utils.Terminal.Translation.getTranslationFor;
import static org.sephire.games.framework4x.core.utils.Validations.areArgumentsNotNull;

/**
 * <p>This screen allows the player to create a new civilization to select in the create game window.</p>
 * <p>Civilizations created this way are stored in the user's home directory to be loaded by the create
 * game screen</p>
 */
@Slf4j
public class CreateCivilizationWindow extends Basic4XWindow {

	private CivilizationForm civilizationForm;
	private WindowEventBus parentEventBus;

	private CreateCivilizationWindow(WindowBasedTextGUI textGUI,WindowEventBus parentEventBus) throws Throwable {
		super(textGUI);

		this.civilizationForm = new CivilizationForm();
		this.parentEventBus = parentEventBus;

		setupFrameConfig()
		  .andThen(this::buildFormElements)
		  .getOrElseThrow(t -> t);

	}

	private static Panel inlineWrap(Component... components) {
		var panel = new Panel(new LinearLayout(Direction.HORIZONTAL));
		List.of(components).forEach(panel::addComponent);

		return panel;
	}

	private Try<Void> buildFormElements() {
		return Try.of(() -> {

			var backgroundPanel = new Panel(new LinearLayout(Direction.VERTICAL));

			// Header
			backgroundPanel.addComponent(
			  new Label(getTranslationFor(Locale.ENGLISH, "createCivilizationWindow.header")
				.getOrElseThrow(() -> new TranslationNotFoundException("createCivilizationWindow.header"))));

			// Form fields
			backgroundPanel.addComponent(
			  createInputField("createCivilizationWindow.identifier", "identifier", false)
				.getOrElseThrow(t -> t));
			backgroundPanel.addComponent(
			  createInputField("createCivilizationWindow.name", "name", false)
				.getOrElseThrow(t -> t));
			backgroundPanel.addComponent(
			  createInputField("createCivilizationWindow.description", "description", true)
				.getOrElseThrow(t -> t));
			backgroundPanel.addComponent(
			  createInputField("createCivilizationWindow.demonym.singular", "demonymSingular", false)
				.getOrElseThrow(t -> t));
			backgroundPanel.addComponent(
			  createInputField("createCivilizationWindow.demonym.plural", "demonymPlural", false)
				.getOrElseThrow(t -> t));

			// Save button
			backgroundPanel.addComponent(
			  new Button(getTranslationFor(Locale.ENGLISH, "createCivilizationWindow.save")
				.getOrElseThrow(() -> new TranslationNotFoundException("createCivilizationWindow.save")), () -> {
				  var civilization = civilizationForm.toCivilization();

				  parentEventBus.fireEvent(new NewCivilizationAddedEvent(civilization));

				  close();
			  }));


			setComponent(backgroundPanel);
			return null;
		});
	}

	private Try<Panel> createInputField(String labelKey, String formField, boolean isTextField) {
		return Try.of(() -> {
			var inputLabel = new Label(getTranslationFor(
			  Locale.ENGLISH,
			  labelKey).getOrElseThrow(() -> new TranslationNotFoundException(labelKey)));

			var inputTextBuilder = EventedTextBox.builder()
			  .withHandler((String newValue) -> {
				  var fieldSetter = CivilizationForm.setterFor(formField)
					.getOrElseThrow(() -> new IllegalArgumentException("Invalid form field specified"));

				  Try.of(() -> fieldSetter.invoke(this.civilizationForm, newValue))
					.getOrElseThrow(() -> new RuntimeException("Error while setting the new value in the civilization form"));

				  return null;
			  });
			if(isTextField){
				inputTextBuilder = inputTextBuilder
				  .enableMultiline()
				  .withSize(30,10);
			}
			var inputText = inputTextBuilder.build().getOrElseThrow(t->t);

			return inlineWrap(inputLabel, inputText);
		});
	}

	public static BuilderGUI builder() {
		return new Builder();
	}

	private Try<Void> setupFrameConfig() {
		return Try.of(() -> {
			setHints(java.util.List.of(Window.Hint.FULL_SCREEN));
			setTitle(getTranslationFor(Locale.ENGLISH, "createCivilizationWindow.title")
			  .getOrElseThrow(() -> new TranslationNotFoundException("createCivilizationWindow.title")));
			setCloseWindowWithEscape(true);

			return null;
		});
	}

	public interface BuilderBuilder {
		Try<CreateCivilizationWindow> build();
	}

	public interface BuilderGUI {
		BuilderEventBus withTextGUI(WindowBasedTextGUI textGUI);
	}

	public interface BuilderEventBus {
		BuilderBuilder withEventBus(WindowEventBus parentEventBus);
	}

	public static class Builder implements BuilderBuilder, BuilderGUI,BuilderEventBus {
		private WindowBasedTextGUI textGUI;
		private WindowEventBus parentBus;

		@Override
		public BuilderEventBus withTextGUI(WindowBasedTextGUI textGUI) {
			this.textGUI = textGUI;
			return this;
		}

		@Override
		public BuilderBuilder withEventBus(WindowEventBus parentEventBus) {
			this.parentBus = parentEventBus;
			return this;
		}

		@Override
		public Try<CreateCivilizationWindow> build() {
			return Try.of(() -> {
				areArgumentsNotNull(textGUI,parentBus).getOrElseThrow(t -> t);
				return new CreateCivilizationWindow(textGUI,parentBus);
			});
		}
	}
}
