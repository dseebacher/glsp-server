package org.eclipse.glsp.server.actions;


// TODO: should it be a request action or just an action?
public class RequestSearchAction extends RequestAction<SearchResultAction> {

    public static final String ID = "requestSearch";

    private String searchTerm;

    public RequestSearchAction() {
        super(ID);
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