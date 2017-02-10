package com.samebug.clients.idea.ui.component.solutions;

import com.intellij.util.messages.MessageBus;
import com.samebug.clients.idea.resources.SamebugBundle;
import com.samebug.clients.idea.ui.component.util.tabbedPane.SamebugTabHeader;
import com.samebug.clients.idea.ui.component.util.tabbedPane.SamebugTabbedPane;

public final class ResultTabs extends SamebugTabbedPane {
    private final MessageBus messageBus;
    private final Model model;

    private final WebResultsTab webResultsTab;
    private final TipResultsTab tipResultsTab;
    private final SamebugTabHeader webResultsTabHeader;
    private final SamebugTabHeader tipResultsTabHeader;

    public ResultTabs(MessageBus messageBus, Model model) {
        this.model = new Model(model);
        this.messageBus = messageBus;

        webResultsTab = new WebResultsTab(messageBus, model.webResults);
        tipResultsTab = new TipResultsTab(messageBus, model.tipResults);

        // TODO show something when no solutions?
        if (model.tipResults.getTipsSize() > 0) {
            tipResultsTabHeader = addTab(SamebugBundle.message("samebug.component.solutionFrame.tips.tabName"), model.tipResults.getTipsSize(), tipResultsTab);
        } else {
            tipResultsTabHeader = null;
        }
        if (model.webResults.getHitsSize() > 0) {
            webResultsTabHeader = addTab(SamebugBundle.message("samebug.component.solutionFrame.webSolutions.tabName"), model.webResults.getHitsSize(), webResultsTab);
        } else {
            webResultsTabHeader = null;
        }


    }

    public static final class Model {
        private final WebResultsTab.Model webResults;
        private final TipResultsTab.Model tipResults;

        public Model(Model rhs) {
            this(rhs.webResults, rhs.tipResults);
        }

        public Model(WebResultsTab.Model webResults, TipResultsTab.Model tipResults) {
            this.webResults = webResults;
            this.tipResults = tipResults;
        }
    }
}
