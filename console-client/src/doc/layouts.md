#Layout system

## Introduction
The main problem to solve is that of resizing components when the size of the terminal changes. 

With absolute values, each component must provide a way to change content based on the size it is allocated, and also 
how should each component grow inside a container when the container's size changes?

The standard way to handle this in CSS is to specify width's in percentages, or using grid layouts, or the like. 
The way to handle this in Swing is to use Layouts, which handle the positioning and sizing of it's components.

There may be many layouts which handle these behaviours differently. For example an AbsoluteLayout will just use the 
coordinates provided by the child components as is. A BorderLayout will divide the screen in different regions where 
it will stuff components with different growing values (more for the center, less for the borders).

Since going the CSS way is quite complex for a terminal screen, layouts is the way I'm choosing. It's a proven method, 
so no need to reinvent the wheel.

Layouts must be able to be nested. A flow layout inside a border layout, for example.

Components should be able to influence the layout with directives. 

## The layouts

The most general layout is the Grid. It defines how many columns and rows the screen is divided into and positions the
elements into these cells as requested by the children. Cells can be merged at the container level to form bigger 
regions.

The flow layout will position children one after the other. A vertical flow will put each children into a new line and
an horizontal flow will put each children in the same line.

A border layout makes use of the grid layout to define fixed positions like north, center, south, east and west and
pre assign growth values to these regions.

### Layout parameters

Each layout will provide configuration, both for the layout itself and for the children that are managed by it.

#### Layout container parameters

A layout container may be parameterized with general configuration, on its regions, the growing parameters, etc.

For example: for a grid layout, how many columns and rows, the spacing configuration for each cell, etc.

#### Layout child parameters

We will call them LayoutChildParameter. Each layout will provide parameters that can be attached to childs when they are 
added to a container. A layout param is defined by a name which will be defined by the Layout class, and a value, 
which can be any object.

For example, when adding a side panel inside a container, we may want it to have a maximum width of 10 chars, instead of
being assigned half the screen by the layout.

Another example is when adding a child to a border layout, we may want it to be in the top region and another one in
the center region.

## Parameterized resizing and positioning

### Growth 

Resizing is the raison d'être of layouts. They should provide a way to automatically resize the contained components.
A layout can resize both its regions and the child components they contain.
For example: 
For a terminal size of 80x24, and a top level container wich has Border layout with a north, center and east regions 
filled with components, let's define how they are initially defined: The north region is 80x3, a menu bar. The east 
region is a side panel of size 10x21. The center region is a container of size 70x21.
If the terminal is resized by the user to have a 120x40 size, how would we like the regions to grow and its components?
For a typical application, the top menu should grow horizontally but not vertically. For this case, both the north
region and its menu container should grow to 120x3 from 80x3. The components inside the menu container, though, should
not change their position or size. So we can say that both the north region and the top menu container have a flexible
width growth, and a fixed vertical size.
For the side panel, we may want it to grow and shrink with resizes, but just to a point. Perhaps we need the side panel
to have _at least_ 10 characters width and _at most_ 20% of the width size. And vertically to grow to the new vertical
size. The side panel container inside the east region should always adjust to 100% of its region size both horizontally 
and vertically. So, for a new 120x40 terminal size, the side panel will grow to size 24x37.
The center region will automatically adjust to the remaining screen size, so that will be 96x37.

In fact, for the border layout, some things should be restricted. For example: north and south regions must have a 
limited height, be it relative with percentages, or fixed with character size, and they always occupy 100% width size.
The west, center and east regions always occupy 100% height of the terminal minus the north and south vertical size. And
they share the 100% width space between each other. The border layout should enforce that this sharing does not conflict.

To configure all this, the layout should provide region parameters.

#### Region parameters

Following the border layout example, we have for each region:
- size boundaries:
	- min size
	- max size
	- fixed size
- size unit:
	- characters
	- percentage
The size boundaries can be combined to say: min size of at least 10% and max size of at most 20%

