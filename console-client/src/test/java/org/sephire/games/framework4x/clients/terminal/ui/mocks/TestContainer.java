package org.sephire.games.framework4x.clients.terminal.ui.mocks;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sephire.games.framework4x.clients.terminal.ui.Coordinates;
import org.sephire.games.framework4x.clients.terminal.ui.Painter;
import org.sephire.games.framework4x.clients.terminal.ui.components.Container;
import org.sephire.games.framework4x.clients.terminal.ui.layouts.Layout;
import org.sephire.games.framework4x.clients.terminal.ui.size.QualifiedSizeValue;
import org.sephire.games.framework4x.clients.terminal.ui.size.Size;
import org.sephire.games.framework4x.clients.terminal.ui.size.SizeUnit;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * A basic test container that does nothing but contain items.
 */
public class TestContainer extends Container {

	public TestContainer(Coordinates coordinates,Layout layout) {
		super(coordinates);
		this.setLayout(layout);
	}

	@Override
	public void draw(Painter painter) {
	}



	public static class Builder {
		private Coordinates coordinates;
		private Layout layout;

		private Builder(Coordinates coordinates) {
			this.coordinates = coordinates;
		}

		public static Builder container() {
			return container(1,1,1,1);
		}
		public static Builder container(int x,int y) {
			return container(x, y,1,1);
		}
		public static Builder container(int x,int y,int width, int height) {
			return new Builder(
				new Coordinates(
						new Location(x,y),
						new Size(
								new QualifiedSizeValue(height,SizeUnit.CHARACTER),
								new QualifiedSizeValue(width,SizeUnit.CHARACTER)
						)
				)
			);
		}

		public BuildStep withLayout(Layout layout) {
			this.layout = layout;
			return new BuildStep(this);
		}

		@RequiredArgsConstructor
		public class BuildStep {
			@NonNull private Builder params;

			public Container build(){
				return new TestContainer(params.coordinates,params.layout);
			}
		}
	}
}
