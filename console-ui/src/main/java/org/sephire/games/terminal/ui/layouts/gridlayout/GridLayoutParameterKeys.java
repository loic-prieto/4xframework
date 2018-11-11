package org.sephire.games.terminal.ui.layouts.gridlayout;

import org.sephire.games.framework4x.clients.terminal.ui.layouts.LayoutParameterKey;
import org.sephire.games.framework4x.core.model.map.Location;

/**
 * A grid layout is composed of cells identified by the cell 2d location.
 */
public enum GridLayoutParameterKeys implements LayoutParameterKey {
    /**
     * This is the cell position inside the grid.
     */
    CELL(Location.class);

    private Class parameterValueClass;

    GridLayoutParameterKeys(Class parameterValueClass) {
        this.parameterValueClass = parameterValueClass;
    }

    public Class getParameterValueClass(){
        return this.parameterValueClass;
    }

}
