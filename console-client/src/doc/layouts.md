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

## Growth and alignment
