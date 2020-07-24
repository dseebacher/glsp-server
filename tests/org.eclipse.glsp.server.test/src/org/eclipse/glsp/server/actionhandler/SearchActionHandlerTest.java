package org.eclipse.glsp.server.actionhandler;

import org.eclipse.glsp.api.action.kind.SearchAction;
import org.eclipse.glsp.api.model.GraphicalModelState;
import org.eclipse.glsp.server.model.DefaultModelStateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchActionHandlerTest {

    private static final String SOME_CLIENT_ID = "some id";
    private static final String SOME_SEARCH_TERM = "some search term";

    @Test
    void foo() {
        GraphicalModelState state = new DefaultModelStateProvider().create(SOME_CLIENT_ID);

        SearchAction action = new SearchAction(SOME_SEARCH_TERM);

        new SearchActionHandler().executeAction(action,state);
    }
}