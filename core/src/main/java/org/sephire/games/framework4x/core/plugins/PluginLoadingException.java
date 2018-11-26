package org.sephire.games.framework4x.core.plugins;

import io.vavr.collection.List;
import org.sephire.games.framework4x.core.Framework4XException;

public class PluginLoadingException extends Framework4XException {

    private List<Throwable> exceptions;

    public PluginLoadingException(List<Throwable> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public String getMessage() {
        return exceptions
                .map(Throwable::getMessage)
                .reduce((a,b)->a+"\n"+b);
    }
}
