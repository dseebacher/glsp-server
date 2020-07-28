package org.eclipse.glsp.server.actionhandler;

import org.eclipse.glsp.api.action.Action;
import org.eclipse.glsp.api.action.kind.SearchAction;
import org.eclipse.glsp.api.action.kind.SearchResultAction;
import org.eclipse.glsp.api.model.GraphicalModelState;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelRoot;

import java.util.*;
import java.util.stream.Collectors;

public class SearchActionHandler extends BasicActionHandler<SearchAction> {

    @Override
    protected List<Action> executeAction(SearchAction actualAction, GraphicalModelState modelState) {
        return Arrays.asList(new SearchResultAction(findSearchTerm(modelState, actualAction.getSearchTerm())));
    }

    private Collection<String> findSearchTerm(GraphicalModelState modelState, String searchTerm) {
        GModelRoot root = modelState.getRoot();
        if (root == null) {
            return Collections.emptyList();
        }

        return root
                .getChildren()
                .stream()
                .map(element -> whateverMap(searchTerm, element))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String whateverMap(String searchTerm, GModelElement element) {
        if (element instanceof GLabel) {
            String text = ((GLabel) element).getText();
            if (text.contains(searchTerm)) {
                return element.getId();
            }
        }
        return null;
    }
}
