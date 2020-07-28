package org.eclipse.glsp.server.actionhandler;

import org.eclipse.emf.common.util.EList;
import org.eclipse.glsp.api.action.Action;
import org.eclipse.glsp.api.action.kind.RequestSearchAction;
import org.eclipse.glsp.api.action.kind.SearchResultAction;
import org.eclipse.glsp.api.model.GraphicalModelState;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelRoot;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchActionHandler extends BasicActionHandler<RequestSearchAction> {

    @Override
    protected List<Action> executeAction(RequestSearchAction actualAction, GraphicalModelState modelState) {
        return Arrays.asList(new SearchResultAction(findSearchTerm(modelState, actualAction.getSearchTerm())));
    }

    private Collection<String> findSearchTerm(GraphicalModelState modelState, String searchTerm) {
        GModelRoot root = modelState.getRoot();
        if (root == null) {
            return Collections.emptyList();
        }

        return findSearchTermInChildren(searchTerm, root.getChildren());
    }

    private List<String> findSearchTermInChildren(String searchTerm, EList<GModelElement> children) {
        return children
                .stream()
                .flatMap(element -> handleElementTypes(searchTerm, element))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Stream<String> handleElementTypes(String searchTerm, GModelElement element) {
        if (element instanceof GLabel) {
            return findSearchTermInText(searchTerm, element);
        } else if (element.getChildren() != null) {
            return findSearchTermInChildren(searchTerm, element.getChildren()).stream();
        }
        return null;
    }

    private Stream<String> findSearchTermInText(String searchTerm, GModelElement element) {
        String text = ((GLabel) element).getText();
        if (text.contains(searchTerm)) {
            return Arrays.asList(element.getId()).stream();
        }
        return null;
    }
}
