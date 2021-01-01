package org.eclipse.glsp.server.actions;

import java.util.Collection;

public class SearchResultAction extends ResponseAction {

    public static final String ID = "search";

    private Collection<String> results;

    public SearchResultAction() {
        super(ID);
    }

    public SearchResultAction(final Collection<String> results) {
        this();
        this.results = results;
    }

    public Collection<String> getResults() {
        return results;
    }
}
