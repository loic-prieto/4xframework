/**
 * 4X Framework - Console client - API - The console client offers an API that plugins that interact with the console can consume. This mainly
        avoid having the client and the related plugin have a cyclic dependency.
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
package org.sephire.games.framework4x.clients.terminal.api.ui.gamewindow;

/**
 * <p>When adding an element to the bottom bar, it must be positioned into one of three slots
 * <ul>
 *     <li>Left</li>
 *     <li>Center</li>
 *     <li>Right</li>
 * </ul>
 * The elements are aligned depending on their slot, and the center position has the remaining space
 * between the center and right slots.
 * </p>
 */
public enum BottomBarPosition {
	Left,
	Center,
	Right;
}
