package org.sephire.games.framework4x.clients.terminal.api.events;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * <p>A very simple in-memory event bus that will register listeners and fire events to those listeners.</p>
 *
 * <p>Its main use is inside Lanterna Windows, so that component can send messages to one another in a decoupled
 * way.</p>
 */
@Slf4j
public class WindowEventBus {

	private Map<Class, List<Consumer>> listeners;

	public WindowEventBus() {
		this.listeners = HashMap.empty();
	}

	/**
	 * <p>≈Add a listener for the given type of event.</p>
	 *
	 * <p>Unfortunately, the java generic system doesn't allow to infer the generic of a
	 * container, so we need to pass the class of the event too</P>
	 *
	 * <p>The listener is a Consumer function that will receive an object of the event type, it will
	 * be called whenever an event of that type is fired</p>
	 *
	 * @param eventClass
	 * @param listener
	 * @param <EVENT_TYPE>
	 */
	public <EVENT_TYPE> void registerListener(Class<EVENT_TYPE> eventClass,Consumer<EVENT_TYPE> listener) {
		var listenersForEvent = listeners.get(eventClass);
		if(listenersForEvent.isEmpty()) {
			listeners = listeners.put(eventClass,List.of(listener));
		} else {
			listeners = listeners.put(eventClass,listenersForEvent.get().append(listener));
		}
	}

	/**
	 * <p>Fire an event of type EVENT_TYPE into the event bus. Listeners registered to react to that
	 * type of event will be called with the object given here in no particular order.</p>
	 *
	 * @param event
	 * @param <EVENT_TYPE>
	 */
	public <EVENT_TYPE> void fireEvent(EVENT_TYPE event) {
		var eventListeners = listeners.get(event.getClass());

		Try.of(()->{
			eventListeners.peek((ls)->{
				ls.forEach((listener)->listener.accept(event));
			});

			return Try.success(null);
		});
	}
}
