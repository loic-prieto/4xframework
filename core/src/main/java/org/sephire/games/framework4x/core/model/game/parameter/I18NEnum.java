package org.sephire.games.framework4x.core.model.game.parameter;

/**
 * This interface is to be implemented by enums that must show values in an I18N UI.
 * @param <ENUM>
 */
public interface I18NEnum<ENUM> {
	/**
	 * The key for the i18n resource containing the label value
	 * for this enum value.
	 */
	String getLabel();
	/**
	 * The key for the i18n resource containing the description value
	 * for this enum value.
	 */
	String getDescription();
}
