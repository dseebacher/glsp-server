package org.eclipse.glsp.server.features.search;

import org.eclipse.glsp.server.actions.ResponseAction;

import java.util.Collection;

public class SearchAction extends ResponseAction {

    public static final String ID = "search";

    private Collection<String> results;

    public SearchAction() {
        super(ID);
    }

    public SearchAction(final Collection<String> results) {
        this();
        this.results = results;
    }

    public Collection<String> getResults() {
        return results;
    }
}
