package org.eclipse.glsp.server.actionhandler;

import org.eclipse.glsp.api.action.Action;
import org.eclipse.glsp.api.action.kind.SearchAction;
import org.eclipse.glsp.api.action.kind.SearchResultAction;
import org.eclipse.glsp.api.model.GraphicalModelState;
import org.eclipse.glsp.graph.*;
import org.eclipse.glsp.graph.builder.AbstractGLabelBuilder;
import org.eclipse.glsp.graph.builder.impl.GLabelBuilder;
import org.eclipse.glsp.graph.builder.impl.GNodeBuilder;
import org.eclipse.glsp.server.model.DefaultModelStateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SearchActionHandlerTest {

    private static final String SOME_CLIENT_ID = "some id";
    private static final String SOME_SEARCH_TERM = "some search term";
    private static final String LABEL_123 = "123";
    private static final String SOME_ELEMENT_ID = "some element id";

    private List<SearchResultAction> resultOf(Collection<String> searchTerms) {
        return Arrays.asList(new SearchResultAction(searchTerms));
    }

    @Test
    void searchOnEmptyModelShouldReturnNoAction() {
        SearchAction requestAction = new SearchAction(SOME_SEARCH_TERM);
        GraphicalModelState state = new DefaultModelStateProvider().create(SOME_CLIENT_ID);


        List<SearchResultAction> expectedResultAction = resultOf(Collections.emptyList());

        List<Action> actualReturnActions = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualReturnActions);
    }

    @Test
    void searchFindsALabelBasedOnText() {
        SearchAction requestAction = new SearchAction(LABEL_123);

        GLabel label = new GLabelBuilder().id(SOME_ELEMENT_ID).build();
        label.setText(LABEL_123);
        GModelRoot root = GraphFactory.eINSTANCE.createGModelRoot();
        root.getChildren().add(label);
        GraphicalModelState state = stateWithRoot(root);


        List<SearchResultAction> expectedResultAction = resultOf(Arrays.asList(label.getId()));

        List<Action> actualResultAction = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualResultAction);
    }

    private void assertEqualActions(List<SearchResultAction> expectedResultAction, List<Action> actualResultAction) {
        Iterator<Action> actualActionIterator = actualResultAction.iterator();

        for (SearchResultAction expectedAction : expectedResultAction) {
            Action actualAction = actualActionIterator.next();
            assertTrue(actualAction instanceof SearchResultAction);
            assertEquals(expectedAction.getResults(), ((SearchResultAction) actualAction).getResults());
        }
    }

    private GraphicalModelState stateWithRoot(GModelRoot root) {
        GraphicalModelState state = new DefaultModelStateProvider().create(SOME_CLIENT_ID);
        state.setRoot(root);
        return state;
    }
}