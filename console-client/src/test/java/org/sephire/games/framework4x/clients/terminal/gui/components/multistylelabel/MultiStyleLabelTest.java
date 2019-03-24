package org.sephire.games.framework4x.clients.terminal.gui.components.multistylelabel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sephire.games.framework4x.clients.terminal.gui.components.MultiStyleLabel;
import org.sephire.games.framework4x.clients.terminal.utils.IntegerRange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultiStyleLabelTest {

	@Test
	@DisplayName("Should create successfully a multi style label when using good parameters")
	public void should_create_multi_style_label_correctly() throws Throwable {
		var text = "A menu option";

		var labelTry = MultiStyleLabel.builder()
		  .forText("A menu option")
		  .addStyledRange(new IntegerRange(0,1)).getOrElseThrow(t->t)
		  .addStyledRange(new IntegerRange(1,text.length())).getOrElseThrow(t->t)
		  .build();

		assertTrue(labelTry.isSuccess());

		var label = labelTry.get();
		assertEquals(2,label.getLabels().size());
		assertEquals("A",label.getLabels().get(0).getText());
		assertEquals(" menu option",label.getLabels().get(1).getText());
	}

	@Test
	@DisplayName("Should throw InvalidRange exception if provided bad ranges")
	public void should_complain_if_invalid_ranges() throws Throwable{
		var text = "A menu option";

		var labelTry = MultiStyleLabel.builder()
		  .forText("A menu option")
		  .addStyledRange(new IntegerRange(0,5)).getOrElseThrow(t->t)
		  .addStyledRange(new IntegerRange(2,4));

		assertTrue(labelTry.isFailure());
		assertEquals(IllegalArgumentException.class,labelTry.getCause().getClass());
	}
}
