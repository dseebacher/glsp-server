package org.eclipse.glsp.server.actionhandler;

import org.eclipse.glsp.api.action.Action;
import org.eclipse.glsp.api.action.kind.SearchAction;
import org.eclipse.glsp.api.model.GraphicalModelState;

import java.util.List;

public class SearchActionHandler extends BasicActionHandler<SearchAction> {
    @Override
    protected List<Action> executeAction(SearchAction actualAction, GraphicalModelState modelState) {
        return null;
    }
}
