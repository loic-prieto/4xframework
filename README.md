# 4X Framework

This project contains a framework to build turn based 4X games, as well as some plugins to test the framework in a
RI environment. 

As it currently is,the frameworks is still being designed and built piece by piece, so this repository is still unusable
for any game maker wishing to implement their next 4X game.

## The Framework

Loosely based on an amalgam of different 4X games, the 4X framework is composed of a core library that contains the
core concepts of a 4X game, without binding itself to any flavour of a civ-like game. It is the core library that the
game maker must use at the minimum. Aside from the core library, everything else is a plugin.

The core library does not offer a client of any kind. This same repository contains a terminal-based client that can be
used to test the game while it is being done.

## The  Standard Plugin

The Standard Plugin provides the core concepts of a civilization-like game, like cities, units, armies, production, 
resources, etc. These concepts are being provided in the standard plugin instead of in the core library because each 
game built with the 4X framework may not use these concepts as defined in the Standard plugin.

The standard plugin also comes with an extension to the console client so that it can be tested in it.

## The Civilization Plugin

Based on the standard plugin, the Civilization plugin provides a Civilization flavour to the Standard plugin. It defines
the assets of the game to be similar to our own Earth and the civilizations that lived in it.

## The technology stack

The 4X Framework is a Java based project. The minimum language version is 11. Although the framework runs inside the JVM
and may be invoked by other languages, everything is designed with Java in mind, including heavy use of classes, DI and
design patterns that belong to Java, so using other languages may be awkward. Nothing prevents, of course, creating adapters
for other JVM languages, but that is far outside of the scope of the first implementation of this framework. 