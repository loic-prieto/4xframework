package org.sephire.games.framework4x.core.model.events;

import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles domain events.
 * We're choosing this form of decoupling for communication between domain entities and the outside world to
 * avoid having the domain objects having to make use of services.
 */
@Slf4j
public class DomainEvents {
	private Map<Class, List<Function1>> eventsListeners;

	private static DomainEvents singletonInstance;
	public static DomainEvents getInstance() {
		if(singletonInstance == null) {
			singletonInstance = new DomainEvents();
		}
		return singletonInstance;
	}

	public DomainEvents() {
		this.eventsListeners = HashMap.empty();
	}

	public <EVENT_TYPE extends DomainEvent> void registerListener(Class<EVENT_TYPE> eventType,Function1<EVENT_TYPE,Void> callback) {
		var listeners = eventsListeners.get(eventType).getOrElse(List::empty);
		eventsListeners = eventsListeners.put(eventType,listeners.append(callback));
	}

	public <EVENT_TYPE extends DomainEvent> void fireEvent(EVENT_TYPE event) {
		var listenersSearch = eventsListeners.get(event.getClass());
		if(listenersSearch.isDefined()) {
			var listeners = listenersSearch.get();
			listeners.forEach(callback->{
				Try.of(()->{
					callback.apply(event);
					return null;
				}).onFailure((t)->{
					log.error("Error while executing listener for event {0}: {1}",event.getClass().toString(),t.getMessage());
				});
			});
		}
	}
}
