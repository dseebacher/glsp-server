package org.eclipse.glsp.server.actionhandler;

import org.eclipse.glsp.api.action.Action;
import org.eclipse.glsp.api.action.kind.RequestSearchAction;
import org.eclipse.glsp.api.action.kind.SearchResultAction;
import org.eclipse.glsp.api.model.GraphicalModelState;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelRoot;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchActionHandler extends BasicActionHandler<RequestSearchAction> {

    @Override
    protected List<Action> executeAction(RequestSearchAction actualAction, GraphicalModelState modelState) {
        return Collections.singletonList(new SearchResultAction(findSearchTerm(modelState, actualAction.getSearchTerm())));
    }

    private Collection<String> findSearchTerm(GraphicalModelState modelState, String searchTerm) {
        GModelRoot root = modelState.getRoot();
        if (root == null) {
            return Collections.emptyList();
        }

        return findSearchTermInChildren(searchTerm, root.getChildren());
    }

    private List<String> findSearchTermInChildren(String searchTerm, List<GModelElement> children) {
        return findSearchTermStreamInChildren(searchTerm, children.stream())
                .collect(Collectors.toList());
    }

    private Stream<String> findSearchTermStreamInChildren(String searchTerm, Stream<GModelElement> children) {
        return children
                .flatMap(element -> handleElementTypes(searchTerm, element))
                .filter(Objects::nonNull);
    }

    private Stream<String> handleElementTypes(String searchTerm, GModelElement element) {
        if (element instanceof GLabel) {
            return Stream.of(findSearchTermInText(searchTerm, element));
        } else if (element.getChildren() != null) {
            return findSearchTermStreamInChildren(searchTerm, element.getChildren().stream());
        }
        return null;
    }

    private String findSearchTermInText(String searchTerm, GModelElement element) {
        String text = ((GLabel) element).getText();
        if (text.contains(searchTerm)) {
            return element.getId();
        }
        return null;
    }
}
