package org.eclipse.glsp.api.action.kind;

import java.util.Collection;

public class SearchResultAction extends ResponseAction {

    private Collection<String> results;

    public SearchResultAction() {
        super(Kind.SEARCH_RESULT);
    }

    public SearchResultAction(final Collection<String> results) {
        this();
        this.results = results;
    }

    public Collection<String> getResults() {
        return results;
    }
}
