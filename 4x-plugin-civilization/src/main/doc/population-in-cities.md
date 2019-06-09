# Standard Plugin - Civilization - Population in cities

The population is at the core of a civilization. Indeed, everything that can be done by a civilization is done though 
the population.

Population grows in cities given the right health conditions. I would like population to grow based on a realistic 
model, taking into account health conditions, access to food, economic classes, time passed between each turn, etc.
Check the population growth document in the standard plugin documentation folder.

The working population, that is, the population that counts to produce goods is a fraction of the total population. 
The non active population has to be supported. When health conditions improve (by modernizing sanitation and having 
better access to food sources), the age expectancy improves too, and the number of people to be supported outside the 
active age increases as an absolute number (as opposed to a relative number, which should remain the same). So, having 
more people gives a definitive advantage to many resources, but also increase the burden to support them, which means 
that some planning is required so that the overall population can support the social needs of the whole.

The active population is represented as the concept of Manpower.

## Manpower

Manpower is a resource. Everything needs manpower to build. Manpower costs should increase with the sophistication of 
what is being built, which should also follow along the growth of the population over time.

Manpower represents not only the physical workers but also their supporting people (bureaucracy, management, 
transport, etc).Manpower can be moved too, either by migration or forced relocation.

Manpower is a resource produced by the city based on population of the city (a percentage representing the active
population, which can be increased or decreased with different policies). It is a non storable resource, since it
represents the amount of work done by the population over a period of time.

### Manpower segregation

It may be interesting to put a cap on the different type of citizens that can exist for a given period of time, 
since it wouldn't be realistic to use population as a fungible resource that can be used for anything, unless we're 
talking about forced labor. Population can be divided on blue collar - white collar and other categories. 
This should be very carefully though out, because it may become unfun to the gameplay.

This is left for other plugins, since going all Victoria-pop-management on this plugin may be too much when starting out.

## Usage of manpower in the cities

Manpower is used for several purposes:
- To build...buildings
- To extract resources
- To work in buildings
- to build units
- to work in buildings in far away colonies 

The manpower need varies from building to building.