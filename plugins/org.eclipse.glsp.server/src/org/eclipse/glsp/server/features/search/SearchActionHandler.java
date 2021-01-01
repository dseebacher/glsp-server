package org.eclipse.glsp.server.features.search;

import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.BasicActionHandler;
import org.eclipse.glsp.server.model.GModelState;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchActionHandler extends
        BasicActionHandler<RequestSearchAction> {

    @Override
    protected List<Action> executeAction(RequestSearchAction actualAction, GModelState modelState) {
        return Collections.singletonList(new SearchAction(findSearchTerm(modelState, actualAction.getSearchTerm())));
    }

    private Collection<String> findSearchTerm(GModelState modelState, String searchTerm) {
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
