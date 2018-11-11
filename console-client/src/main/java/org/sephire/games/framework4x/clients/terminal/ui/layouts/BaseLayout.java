package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class BaseLayout implements Layout {
	@NonNull @Getter
	private Option<Container> container;
	@Getter
	private Map<UUID,Map<LayoutParameterKey,Object>> childrenParameters = HashMap.empty();
	@Getter @Setter
	private Map<UUID, Coordinates> childrenCoordinates = HashMap.empty();

	@Override
	public void addChild(UIElement child, Map<LayoutParameterKey, Object> parameters) {
	    // Quick check that the value of the parameters are correct
		Set<LayoutParameterKey> invalidKeys = parameters.filter((name, value) -> !value.getClass().equals(name.getParameterValueClass()))
                .keySet();
		if(!invalidKeys.isEmpty()) {
			throw new InvalidParameterValueClass(invalidKeys);
		}

		childrenParameters = childrenParameters.put(child.getIdentifier(),parameters);
		this.updateChildrenCoordinates();
	}

	@Override
	public Option<Coordinates> getChildCoordinates(UIElement childElement) {
		return childrenCoordinates.get(childElement.getIdentifier());
	}

	public void setContainer(Container container) {
		this.container = Option.of(container);
	}

}
