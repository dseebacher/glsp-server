package org.eclipse.glsp.server.actionhandler;

import org.eclipse.glsp.api.action.Action;
import org.eclipse.glsp.api.action.kind.SearchAction;
import org.eclipse.glsp.api.action.kind.SearchResultAction;
import org.eclipse.glsp.api.model.GraphicalModelState;
import org.eclipse.glsp.graph.GGraph;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.graph.GraphFactory;
import org.eclipse.glsp.graph.builder.impl.GLabelBuilder;
import org.eclipse.glsp.server.model.DefaultModelStateProvider;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SearchActionHandlerTest {

    private static final String SOME_CLIENT_ID = "some id";
    private static final String SOME_SEARCH_TERM = "some search term";
    private static final String SOME_ELEMENT_ID = "some element id";
    private static final String LABEL_123 = "123";
    private static final String LABEL_456 = "456";

    @Test
    void searchOnEmptyModelShouldReturnNoId() {
        SearchAction requestAction = new SearchAction(SOME_SEARCH_TERM);

        GraphicalModelState state = stateWithRoot(null);


        List<SearchResultAction> expectedResultAction = resultOf(Collections.emptyList());

        List<Action> actualReturnActions = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualReturnActions);
    }

    @Test
    void searchFindsALabelBasedOnText() {
        SearchAction requestAction = new SearchAction(LABEL_123);

        GLabel label = label123();
        GModelRoot root = rootWithLabel(label);
        GraphicalModelState state = stateWithRoot(root);


        List<SearchResultAction> expectedResultAction = resultOf(Arrays.asList(label.getId()));

        List<Action> actualResultAction = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualResultAction);
    }

    @Test
    void searchCantFindALabelBasedOnText() {
        SearchAction requestAction = new SearchAction(LABEL_456);

        GLabel label = label123();
        GModelRoot root = rootWithLabel(label);
        GraphicalModelState state = stateWithRoot(root);


        List<SearchResultAction> expectedResultAction = resultOf(Collections.emptyList());

        List<Action> actualResultAction = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualResultAction);
    }

    @Test
    void searchFindsANestedLabelBasedOnText() {
        SearchAction requestAction = new SearchAction(LABEL_123);

        GLabel label = label123();
        GGraph graph = GraphFactory.eINSTANCE.createGGraph();
        graph.getChildren().add(label);
        GModelRoot root = GraphFactory.eINSTANCE.createGModelRoot();
        root.getChildren().add(graph);
        GraphicalModelState state = stateWithRoot(root);


        List<SearchResultAction> expectedResultAction = resultOf(Arrays.asList(label.getId()));

        List<Action> actualResultAction = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualResultAction);
    }

    private List<SearchResultAction> resultOf(Collection<String> searchTerms) {
        return Arrays.asList(new SearchResultAction(searchTerms));
    }

    private GModelRoot rootWithLabel(GLabel label) {
        GModelRoot root = GraphFactory.eINSTANCE.createGModelRoot();
        root.getChildren().add(label);
        return root;
    }

    private GraphicalModelState stateWithRoot(GModelRoot root) {
        GraphicalModelState state = new DefaultModelStateProvider().create(SOME_CLIENT_ID);
        state.setRoot(root);
        return state;
    }

    private GLabel label123() {
        GLabel label = new GLabelBuilder().id(SOME_ELEMENT_ID).build();
        label.setText(LABEL_123);
        return label;
    }

    private void assertEqualActions(List<SearchResultAction> expectedResultAction, List<Action> actualResultAction) {
        Iterator<Action> actualActionIterator = actualResultAction.iterator();

        for (SearchResultAction expectedAction : expectedResultAction) {
            Action actualAction = actualActionIterator.next();
            assertTrue(actualAction instanceof SearchResultAction);
            assertEquals(expectedAction.getResults(), ((SearchResultAction) actualAction).getResults());
        }
    }
}