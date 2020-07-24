package org.eclipse.glsp.api.action.kind;

import org.eclipse.glsp.api.action.Action;

// TODO: should it be a request action or just an action?
public class SearchAction extends Action {

    private String searchTerm;

    public SearchAction() {
        super(Action.Kind.SEARCH);
    }

    public SearchAction(final String searchTerm) {
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
