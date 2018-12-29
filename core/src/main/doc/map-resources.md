# Defining and providing maps for the games

Although a 4X game typically generates its maps randomly, we may also want to provide for specific maps, like scenarios.

The mechanism to load both is similar.

There are two ways to define maps: via a yaml file that defines every cell, or via code with a map provider.

## Stored/Fixed maps 

Maps stored in yaml files inside the package pluginName.maps will be loaded automatically by the core framework.

A map loaded as a resource will be wrapped at runtime inside a FixedMapGenerator class, which is a kind of MapGenerator.

TBD

## Map generators

A plugin can define any number of maps. Those maps can be generated at runtime with a function. That function can either
load maps from files, or can generate them procedurally.

To be recognized as a map provider, a class must be annotated as such.

```java
@MapProvider
public class PluginTestMapGenerator {
	
	@MapGenerator(name="hills_generator",displayName="plugintest.i18n.hills_generator_name")
	public Try<GameMap> generateRandomMap() {
		return GameMap.builder()
          .addZone(MapZone.builder()
            .withName("level0")
            .withDefaultCells(new Size(20,20), PluginTerrainType.HILL)
            .build().get())
          .withDefaultZone("level0")
          .build()
          .get();    
	}
}
```

This class can live anywhere inside the plugin root package, it will be scanned at plugin load time. 
The only restriction is that the field name of the MapGenerator annotation must be unique among all plugins.

Note that a map generator is invoked when creating a game and may be parameterized. Meanwhile, a fixed map is already
built into the plugin, be it programmatically or in a resource file, and is always the same between all games. A map
generator may produce a different map each time it is used.

## Map availability when creating a game

When creating a game, a user may choose between all maps provided by the different plugins. 

All the fixed or dynamic map generators live inside the same config key: CoreConfigKeyEnum.MAPS
This config key stores a list of map generators that were found when loading a set of plugins.