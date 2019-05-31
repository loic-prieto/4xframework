# Standard Plugin - Production in cities

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

Manpower is also a resource that can be both expended permanently or temporarily (for example, 1000 citizens may be 
needed to build a tank and will be available the next turn, and 100 expended to outfit the unit itself). 
Everything needs manpower to build. Manpower costs should increase with the sophistication of what is being built, 
which should also follow along the growth of the population over time.

Manpower represents not only the physical workers but also their supporting people (bureaucracy, management, 
transport, etc).Manpower can be moved too, either by migration or forced relocation.

Manpower is a resource produced by the city based on population of the city (a percentage representing the active
 population, which can be increased or decreased with different policies). Check the population in cities document
 for more info.

A city produces population based on "realistic" human growth model, which takes into account health and food, economics
 of the population, age, etc, as explained in the documentation. Check the population growth document for more info.
