package org.sephire.games.terminal.ui.layouts;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.components.UIElement;

@RequiredArgsConstructor
public abstract class BaseLayout implements Layout {
	@NonNull @Getter
	private Option<Container> container;

	private List<Tuple2<UIElement,Map<LayoutParameterKey,Object>>> childParameters;

	@Override
	public void addChild(UIElement child, Map<LayoutParameterKey, Object> parameters) {
	    // Quick check that the value of the parameters are correct
		Set<LayoutParameterKey> invalidKeys = parameters.filter((name, value) -> !value.getClass().equals(name.getParameterValueClass()))
                .keySet();
		if(!invalidKeys.isEmpty()) {
			throw new InvalidParameterValueClass(invalidKeys);
		}

		childParameters = childParameters.append(new Tuple2<>(child,parameters));
		updateChildrenCoordinates();
	}

	public void setContainer(Container container) {
		this.container = Option.of(container);
	}

}
