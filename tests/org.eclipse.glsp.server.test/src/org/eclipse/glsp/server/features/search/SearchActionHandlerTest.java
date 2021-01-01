package org.eclipse.glsp.server.features.search;

import org.eclipse.glsp.graph.GGraph;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.graph.GraphFactory;
import org.eclipse.glsp.graph.builder.impl.GLabelBuilder;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.model.DefaultModelStateProvider;
import org.eclipse.glsp.server.model.GModelState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;


public class SearchActionHandlerTest {

    private static final String SOME_CLIENT_ID = "some id";
    private static final String SOME_SEARCH_TERM = "some search term";
    private static final String SOME_ELEMENT_ID = "some element id";
    private static final String LABEL_123 = "123";
    private static final String LABEL_456 = "456";

    @Test
    void searchOnEmptyModelShouldReturnNoId() {
        RequestSearchAction requestAction = new RequestSearchAction(SOME_SEARCH_TERM);

        GModelState state = stateWithRoot(null);


        List<SearchAction> expectedResultAction = resultOf(Collections.emptyList());

        List<Action> actualReturnActions = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualReturnActions);
    }

    @Test
    void searchFindsALabelBasedOnText() {
        RequestSearchAction requestAction = new RequestSearchAction(LABEL_123);

        GLabel label = label123();
        GModelRoot root = rootWithLabel(label);
        GModelState state = stateWithRoot(root);


        List<SearchAction> expectedResultAction = resultOf(Collections.singletonList(label.getId()));

        List<Action> actualResultAction = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualResultAction);
    }

    @Test
    void searchCantFindALabelBasedOnText() {
        RequestSearchAction requestAction = new RequestSearchAction(LABEL_456);

        GLabel label = label123();
        GModelRoot root = rootWithLabel(label);
        GModelState state = stateWithRoot(root);


        List<SearchAction> expectedResultAction = resultOf(Collections.emptyList());

        List<Action> actualResultAction = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualResultAction);
    }

    @Test
    void searchFindsANestedLabelBasedOnText() {
        RequestSearchAction requestAction = new RequestSearchAction(LABEL_123);

        GLabel label = label123();
        GGraph graph = GraphFactory.eINSTANCE.createGGraph();
        graph.getChildren().add(label);
        GModelRoot root = GraphFactory.eINSTANCE.createGModelRoot();
        root.getChildren().add(graph);
        GModelState state = stateWithRoot(root);


        List<SearchAction> expectedResultAction = resultOf(Collections.singletonList(label.getId()));

        List<Action> actualResultAction = new SearchActionHandler().executeAction(requestAction, state);

        assertEqualActions(expectedResultAction, actualResultAction);
    }

    private List<SearchAction> resultOf(Collection<String> searchTerms) {
        return Collections.singletonList(new SearchAction(searchTerms));
    }

    private GModelRoot rootWithLabel(GLabel label) {
        GModelRoot root = GraphFactory.eINSTANCE.createGModelRoot();
        root.getChildren().add(label);
        return root;
    }

    private GModelState stateWithRoot(GModelRoot root) {
        GModelState state = new DefaultModelStateProvider().create(SOME_CLIENT_ID);
        state.setRoot(root);
        return state;
    }

    private GLabel label123() {
        GLabel label = new GLabelBuilder().id(SOME_ELEMENT_ID).build();
        label.setText(LABEL_123);
        return label;
    }

    private void assertEqualActions(List<SearchAction> expectedResultAction, List<Action> actualResultAction) {
        Iterator<Action> actualActionIterator = actualResultAction.iterator();

        for (SearchAction expectedAction : expectedResultAction) {
            Action actualAction = actualActionIterator.next();
            Assertions.assertTrue(actualAction instanceof SearchAction);
            Assertions.assertEquals(expectedAction.getResults(), ((SearchAction) actualAction).getResults());
        }
    }
}