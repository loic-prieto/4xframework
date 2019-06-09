# Standard Plugin - Production in cities

## Overview 
Unit, public works and buildings production should be dependent on the flavor plugin, since it is related greatly to 
how you conceptually define the production and how you want resources to interact with production. For example, the 
Standard Plugin - Civilization, which is a civilization-like clone for the 4x framework will define resources such as
iron, rubber, oil, uranium, etc. The types of buildings available and what bonuses they provide. The type of units that
can be built depending on the technology unlocked. 

Even then, the standard plugin will define some basic concepts of the production system I'm leaning into: a resource 
based production system. 
Some resources are infinite and readily available, such as wood, stone and copper, but other more rare minerals or 
chemicals should be harvested to be able to use them. For example, iron to produce steel-based units or buildings, oil
for motorized units, rubber for vehicles, etc.

## Resources

Civilizations can extract resources from the map. For example:
- iron
- copper
- rubber
- oil
- etc

The manner into which the resource is extracted is left to the flavour plugin. The standard plugin only defines the 
existence of resources and that they may be used when building things.

The resources can be both natural and artificial. For example,steel may be produced from iron in your cities, plastic
may be produced from petrol,food may be produced from farm's produce, slaves may be produced from the population.

Resources are extracted and accumulated into a city warehouse where they can be used. A city can only use resources if
they are stored in its warehouse. With roads, rivers or seas, the resources may be transported. Resource streams should
be configured to share resources between all cities that need them and the production point. 

### Resources streams

A resource is produced/extracted in some way and can be distributed on complex routes that may include cities from other
civilizations.

A route can only go through roads/rivers/sea/air. Although this should be left to the flavour plugin. The cost of the 
route should be decided by that plugin too.

A route can serve many cities and can be instantaneous or take more time depending on the position of the city regarding
the position of the producer.

A route is setup by the producer. Benefits extracted from that route aside from the resource distribution are defined by
other plugins (for example, a commercial treaty may give a fixed amount of money per turn in exchange for a route)

A route is composed of paths between cities. In each end of the path the resource may be stored at that city. If there
are more resources left after the passage through a city, the stream may continue. Not all nodes in the path need to
receive the merchandise (for example, a city may just serve as a communication bridge between cities in different 
continents).

A producer defines the rhythm of distribution, that is, how much resources per turn are given downstream. If a route
communicates n cities that each want a share of the pie, the producer must send enough resources from its stores to that 
route so that each city downstream has its needs satisfied.

### Storable resources

Some resources may be produced but may not be able to be stored, just consumed and then discarded. For example:
- Electricity
- Manpower
A generalization of this concept may be: a given resource has a time to live, defined in turns. Each flavour plugin may
define what resources are available and they ttl. Some plugins may not even use perishable resources at all.

For example, food may be storable for 5 turns. Electricity can only be used or discarded. Iron ore may be storable 
forever.

