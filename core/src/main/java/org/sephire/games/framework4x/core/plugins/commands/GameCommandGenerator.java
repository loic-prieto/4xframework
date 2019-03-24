package org.sephire.games.framework4x.core.plugins.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Used to mark a method that will generate 1 to n game commands</p>
 * <p>The signature of the method may be one of the following:<ul>
 *     <li>public Try&lt;GameCommand&gt; methodName() </li>
 *     <li>public Try&lt;List&lt;GameCommandCategory&gt;&gt; methodName() </li>
 *     <li>public Try&lt;GameCommandCategory&gt; methodName() </li>
 * </ul>
 * The List object must be of type io.vavr.collection.List or java.util.List
 * </p>
 * <p>
 *     When referencing game command categories, these categories must exist or must be
 *     created in the method that references them, otherwise the insertion will fail.<br/>
 *     Referencing categories from parent plugins will work because they will have been loaded
 *     before calling this method.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameCommandGenerator {
}
