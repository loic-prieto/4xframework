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

