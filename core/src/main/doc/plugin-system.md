# The plugin system

The 4X Framework works around a plugin system, where the framework provides basic 
concepts and the plugins implement those concepts.

For example, the framework may have the concept of terrain inside a map, but it doesn't
define what available terrains exist.

Any 4X game client has to ensure that plugins are loaded to start the application.

## The core platform

The 4X framework consists of a core platform jar and a constellation of plugins that
hook themselves to that framework. Each plugin overloads data defined in the core and
in previously loaded plugins.

But the platform cannot launch itself as an application. Rather, an application makes 
use of the 4X core library and any number of plugins to compose a 4X game. An application
that makes use of the core platform to orchestrate a game is considered a client.

## Plugins

Plugins are designed to implement concepts defined in the core platform, and/or redefine
data previously defined in other plugins.

They are loaded in order, and each new plugin overrides previous plugins.

### Base plugins

Base plugins implement the concepts defined in the core platform module. For example, 
the concept terrain is defined in the core module, but no terrain type is defined. A base
module will provide basic terrain types.

Other plugins will be derived plugins which exist only with another base plugin.

### Derived plugins

While base plugins implement all that is needed by the core framework to be able to create a game, derived plugins
complement base plugins with additional info, or behaviour. They can also serve as bridges to a particular client.
For example, the framework ships with a standard base plugin which is civ-like in features. There's a standard for 
terminal client plugin, that allows the standard plugin to work with the terminal client.

Content declared in the derived plugin will override content defined in the base plugin and previously loaded plugins.

### Using a plugin

First and foremost, the plugin we want to use must be in the classpath of the client we launch. There's no convention,
although, at this stage of the development, we put every jar inside the same folder and declare the classpath to be that
folder. Take a look at the Dockerfile of the project to check how we launch the terminal client with the standard plugins.

Once a plugin is in the classpath, it is visible to the plugin loader. When creating a game, the client must declare
which plugins are to be loaded. This can be fetched from a selection given by the user, or with a fixed one. Each plugin,
on initialization, will automatically load every resource that the core recognizes. For the other resources, each plugin
may define a main class (by default, Main class inside the root package of the plugin) that will initialize everything
else.
