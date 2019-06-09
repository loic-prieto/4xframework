# Standard plugin - Civilization - Cities

## Overview

Cities are the same centers of productions as defined in the standard plugin. But we will further refine the concept of
city with the notion of core cities and colonies, which means cities may have a type which restricts what can be done
on them and how the different rules of population growth and production behave.

## City type

### Core city

A core city is a city fully integrated into the civilization. There is no restriction on what can be done in a city.

### Colony 

A colony is mainly a resource extraction and production center. Population doesn't grow and has to be imported from
other cities. The general idea is that these are extensions of a civilization into foreign territory where you don't 
want to found a city, but you still want to extract resources. A colony doesn't incur the same political threat that a 
city does when building it near another civilization. For example, if you found a city near another civilization's 
territory, that may be seen as very threatening, while an extractive colony may just be seen as a nuisance, because
a colony can only build units and buildings related to the colony type. Resource 'theft', though, is still something
that will annoy another civilization, specially if they want those resources for themselves.

In a colony, there is no population growth, and indeed the population that extracts the resources are supported from any 
number of cities that can send manpower to the colony. There is a cost in sending manpower from a city to a colony to
work on the resources.

A colony doesn't have a loyalty radius, but rather an extraction radius which represents how far can the infrastructure 
of the colony allow to extract resources from. Since there is no loyalty radius, a colony may exist inside the borders
of another country. Only if that country allows for the usage of foreign colonies.

Due to the burden of supporting a colony, there must be an advantage, otherwise full cities would be built instead.

#### Colony benefits

- It is a purely extractive center, and no population management has to be performed since it comes from supporting cities.
- it incurs no foreign political penalty
- can purge undesirable elements of society with harsh conditions (but it increases militancy)
- can be promoted to city if not inside foreign borders at any time

### Colony drawbacks

- A foreign power can destroy it by denying access (and then claiming it for itself or destroying it outright) if inside 
its own borders 
- depending on the type of territory, extra health and food provisions will have to be provided
- must provide transportation to the colony, otherwise no resource can be used back

## Manpower in cities

A percentage of the total population can be used to produce things. It is a non-storable resource that either gets used
or gets discarded per turn. 
The percentage of the population depends on the policies and the age distribution of the population in the city. For example,
you may set the active age from 14 to 60. That, depending on the health conditions and facilities may represent 80% of
the population.
Education affects the lower bar of the active population. The more there is education investment, the higher the age at
which the active population is drawn from.
Health conditions affect the upper bar of the active population. Starting from age 60, people have to be supported if not
in the active labor market.