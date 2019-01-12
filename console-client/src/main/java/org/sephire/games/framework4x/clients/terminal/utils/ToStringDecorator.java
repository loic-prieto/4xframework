/**
 * 4X Framework - Console client - A terminal-based client for the 4X framework
 * Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sephire.games.framework4x.clients.terminal.utils;

import io.vavr.Function1;

import static java.lang.String.format;

/**
 * <p>The purpose of this class is to decorate any kind of object to provide a toString method which will
 * be called by methods that expect that object to provide a meaningful toString method.</p>
 * <p>This decorator is needed because we want any given object to not be forced to implement a toString method
 * for other objects (which only serves for one occasion).</p>
 *
 * <p>
 *     Usage: <pre><code>
 *         Function1<PluginSpec,String> stringifier = (pluginSpec) -> pluginSpec.getPluginName();
 *
 *         CheckBoxList<ToStringDecorator<PluginSpec>> checkboxList = new CheckBoxList<>();
 *         plugins
 *           .map((p)->new ToStringDecorator(p,stringifier))
 *           .forEach(checkboxList::addItem);
 *     </code></pre>
 * </p>
 *
 * <p>
 *     A ToStringDecorator delegates compareTo, equals and hashCode methods to the wrapped instance, to honour
 *     the wrapped class behaviour inside collections
 * </p>
 * @param <WRAPPED_CLASS>
 */
public class ToStringDecorator<WRAPPED_CLASS> implements Comparable<ToStringDecorator<WRAPPED_CLASS>> {

	private WRAPPED_CLASS wrappedObject;
	private Function1<WRAPPED_CLASS,String> stringifyer;

	public ToStringDecorator(WRAPPED_CLASS wrappedObject, Function1<WRAPPED_CLASS,String> stringifyer) {
		this.wrappedObject = wrappedObject;
		this.stringifyer = stringifyer;
	}

	public WRAPPED_CLASS getWrappedObject() {
		return wrappedObject;
	}

	/**
	 * Delegates to the wrapped instance.
	 * @return
	 */
	@Override
	public String toString() {
		return stringifyer.apply(wrappedObject);
	}

	/**
	 * Delegates to the wrapped instance.
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		return wrappedObject.equals(((ToStringDecorator<WRAPPED_CLASS>)o).wrappedObject);
	}

	/**
	 * Delegates to the wrapped instance.
	 * @return
	 */
	@Override
	public int hashCode() {
		return wrappedObject.hashCode();
	}

	/**
	 * Delegates to the wrapped instance.
	 * If the instance does not implement the Comparable interface, it will throw {@link UnsupportedOperationException}
	 * @return
	 */
	@Override
	public int compareTo(ToStringDecorator<WRAPPED_CLASS> o) {
		if(!Comparable.class.isAssignableFrom(wrappedObject.getClass())) {
			throw new UnsupportedOperationException(
			  format("The wrapped object %s does not implement the Comparable interface",wrappedObject.getClass()));

		}

		return ((Comparable<WRAPPED_CLASS>)wrappedObject).compareTo(o.wrappedObject);
	}


}
