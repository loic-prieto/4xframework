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

		Consumer<ExamplePayload> listener = (payload) -> wasCalled.setValue(payload.getData());

		WindowEventBus eventBus = new WindowEventBus();
		eventBus.registerListenerFor("eventTest",listener);
		eventBus.fireEvent("eventTest",new ExamplePayload("testData"));

		assertEquals("testData",wasCalled.getValue());
	}

	@Test
	@DisplayName("Given an event bus, if a component fires an event, it should be received only by the correct listener")
	public void should_send_event_to_listener(){
		var listener1State = new MutableValue<>("");
		var listener2State = new MutableValue<>("");

		Consumer<ExamplePayload> listener1 = (payload) -> listener1State.setValue(payload.getData());
		Consumer<ExamplePayload> listener2 = (payload) -> listener2State.setValue(payload.getData());

		WindowEventBus eventBus = new WindowEventBus();
		eventBus.registerListenerFor("eventTest1",listener1);
		eventBus.registerListenerFor("eventTest2",listener2);
		eventBus.fireEvent("eventTest1",new ExamplePayload("testData"));

		assertEquals("testData",listener1State.getValue());
		assertEquals("",listener2State.getValue());
	}

	@Test
	@DisplayName("Given an event bus, if a listener fails for some reason, the bus should gobble the error")
	public void the_event_bus_should_gobble_listener_errors(){

		Consumer<ExamplePayload> listener = (payload) ->{ throw new RuntimeException("error");};

		WindowEventBus eventBus = new WindowEventBus();
		eventBus.registerListenerFor("eventTest",listener);
		var eventSendingExecution = Try.of(()->{
			eventBus.fireEvent("eventTest",new ExamplePayload("testData"));
			return null;
		});

		assertTrue(eventSendingExecution.isSuccess());
	}
}

@AllArgsConstructor
@Getter
class ExamplePayload {
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