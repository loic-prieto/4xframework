/**
 * 4X Framework - Console client - API - The console client offers an API that plugins that interact with the console can consume. This mainly
        avoid having the client and the related plugin have a cyclic dependency.
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 4X Framework - Console client - API - The console client offers an API that plugins that interact with the console can consume. This mainly
        avoid having the client and the related plugin have a cyclic dependency.
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.clients.terminal.api.events;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WindowEventBusTest {

	@Test
	@DisplayName("Given an event bus, if a component fires an event, it should be received by a listener")
	public void should_send_event_to_listener(){
		var wasCalled = new MutableValue<>("");

		Consumer<ExampleEvent> listener = (payload) -> wasCalled.setValue(payload.getData());

		WindowEventBus eventBus = new WindowEventBus();
		eventBus.registerListener(ExampleEvent.class,listener);
		eventBus.fireEvent(new ExampleEvent("testData"));

		assertEquals("testData",wasCalled.getValue());
	}

	@Test
	@DisplayName("Given an event bus, if a component fires an event, it should be received only by the correct listener")
	public void should_send_event_to_correct_listener(){
		var listener1State = new MutableValue<>("");
		var listener2State = new MutableValue<>("");

		Consumer<ExampleEvent> listener1 = (payload) -> listener1State.setValue(payload.getData());
		Consumer<ExampleEvent2> listener2 = (payload) -> listener2State.setValue(payload.getData());

		WindowEventBus eventBus = new WindowEventBus();
		eventBus.registerListener(ExampleEvent.class,listener1);
		eventBus.registerListener(ExampleEvent2.class,listener2);
		eventBus.fireEvent(new ExampleEvent("testData"));

		assertEquals("testData",listener1State.getValue());
		assertEquals("",listener2State.getValue());
	}

	@Test
	@DisplayName("Given an event bus, if a listener fails for some reason, the bus should gobble the error")
	public void the_event_bus_should_gobble_listener_errors(){

		Consumer<ExampleEvent> listener = (payload) ->{ throw new RuntimeException("error");};

		WindowEventBus eventBus = new WindowEventBus();
		eventBus.registerListener(ExampleEvent.class,listener);
		var eventSendingExecution = Try.of(()->{
			eventBus.fireEvent(new ExampleEvent("testData"));
			return null;
		});

		assertTrue(eventSendingExecution.isSuccess());
	}
}

@AllArgsConstructor
@Getter
class ExampleEvent {
	private String data;
}

@AllArgsConstructor
@Getter
class ExampleEvent2 {
	private String data;
}


class MutableValue<T> {

	@Getter
	@Setter
	T value;

	public MutableValue(T value) {
		this.value = value;
	}
}