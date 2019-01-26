/**
 * 4X Framework - Core library - The core library on which to base the game
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.core.plugins.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be used on methods of a @PluginLifecycleHandler annotated class that
 * handle the event of a game using this plugin being created.
 *
 * This method is called when all other parent plugins have been called, and is a chance to
 * add additional game state for a game. All game parameters defined by this plugin and its parents
 * are loaded in the configuration at this point, so they can be used to manipulate the game.
 *
 * There can only be one method in the class with this annotation, a design decision to simplify
 * the API.
 *
 * The signature of the method must accept a Configuration parameter that will hold all
 * the configuration data loaded up until that point and will receive a Game object.
 * The method must return a Try object, whose correct value won't be used.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GameLoadingHook {
}
