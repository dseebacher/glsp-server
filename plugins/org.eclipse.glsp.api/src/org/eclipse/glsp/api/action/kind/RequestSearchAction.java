package org.eclipse.glsp.api.action.kind;

import org.eclipse.glsp.api.action.Action;

// TODO: should it be a request action or just an action?
public class RequestSearchAction extends RequestAction<SearchResultAction> {

    private String searchTerm;

    public RequestSearchAction() {
        super(Action.Kind.SEARCH);
    }

    public RequestSearchAction(final String searchTerm) {
        this();
        this.searchTerm = searchTerm;
    }


    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
