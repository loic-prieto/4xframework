package org.sephire.games.framework4x.clients.terminal.ui.layouts;

import io.vavr.collection.Set;
import org.sephire.games.framework4x.clients.terminal.FourXFrameworkClientException;

/**
 * This exception happens when adding a child item to a container with layout parameters
 * and one of the parameter value is of a different class as the one expected.
 * A layout parameter has a key and a value, and the key defines what class the value must be.
 */
public class InvalidParameterValueClass extends FourXFrameworkClientException {
    private Set<LayoutParameterKey> invalidKeys;

    public InvalidParameterValueClass(Set<LayoutParameterKey> invalidKeys) {
        this.invalidKeys = invalidKeys;
    }

    @Override
    public String getMessage() {
        String keys = invalidKeys
                .map(key -> key.toString())
                .reduce((key1,key2) -> key2 != null ? key1+","+key2 : key1);

        return "The following keys have been assigned invalid values: "+keys;
    }
}
