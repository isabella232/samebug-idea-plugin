package com.samebug.clients.idea.ui.controller.search;

import com.intellij.openapi.diagnostic.Logger;
import com.samebug.clients.common.services.SearchService;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.idea.ui.component.card.ExternalSolutionView;
import com.samebug.clients.idea.ui.component.card.SamebugTipView;
import com.samebug.clients.idea.ui.component.organism.MarkPanel;
import com.samebug.clients.idea.ui.component.tab.SearchTabView;
import com.samebug.clients.search.api.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Converting hits from model to view.
 *
 * The purpose of this class is to enrich raw data of solution hits with functionality required by the views.
 * To do so, this is actually a controller, accessing the context of the plugin, e.g. current user, and for
 * convenience, it also accesses the current solutions.
 */
final class HitConverter {
    final static Logger LOGGER = Logger.getInstance(ModelController.class);
    @NotNull
    final SearchTabController controller;

    public HitConverter(@NotNull final SearchTabController controller) {
        this.controller = controller;
    }

    public SearchTabView.Model convertSolutions(@NotNull final Solutions solutions) {
        return new SearchTabView.Model() {

            @Override
            public SearchGroup getSearch() {
                return solutions.searchGroup;
            }

            @Override
            public List<ExternalSolutionView.Model> getReferences() {
                List<ExternalSolutionView.Model> result = new ArrayList<ExternalSolutionView.Model>(solutions.references.size());
                for (RestHit<SolutionReference> reference : solutions.references) {
                    result.add(convertReference(solutions.searchGroup, reference));
                }
                return result;
            }

            @Override
            public List<SamebugTipView.Model> getTips() {
                List<SamebugTipView.Model> result = new ArrayList<SamebugTipView.Model>(solutions.tips.size());
                for (RestHit<Tip> tip : solutions.tips) {
                    result.add(convertTip(solutions.searchGroup, tip));
                }
                return result;
            }
        };
    }

    @Nullable
    public MarkPanel.Model convertHit(@NotNull final RestHit hit) {
        SearchGroup search = currentSearch();
        if (search == null) return null;
        else return convertHit(search, hit);
    }


    private MarkPanel.Model convertHit(@NotNull final SearchGroup search, @NotNull final RestHit hit) {
        if (hit.solution instanceof Tip) return convertTip(search, hit);
        else if (hit.solution instanceof SolutionReference) return convertReference(search, hit);
        else throw new UnsupportedOperationException("Cannot handle solution type " + hit.solution.getClass().getSimpleName());
    }

    private SamebugTipView.Model convertTip(final SearchGroup search, final RestHit<Tip> hit) {
        return new SamebugTipView.Model() {
            @NotNull
            @Override
            public RestHit<Tip> getHit() {
                return hit;
            }

            @NotNull
            @Override
            public int getSearchId() {
                return search.getLastSearch().id;
            }

            @NotNull
            @Override
            public List<BreadCrumb> getMatchingBreadCrumb() {
                return SearchService.getMatchingBreadCrumb(search, hit);
            }

            @Override
            public boolean canBeMarked() {
                return SearchService.canBeMarked(currentUserId(), search, hit);
            }

            @Override
            public boolean createdByCurrentUser() {
                return SearchService.createdByUser(currentUserId(), hit);
            }
        };
    }

    private ExternalSolutionView.Model convertReference(final SearchGroup search, final RestHit<SolutionReference> hit) {
        return new ExternalSolutionView.Model() {

            @NotNull
            @Override
            public RestHit<SolutionReference> getHit() {
                return hit;
            }

            @NotNull
            @Override
            public int getSearchId() {
                return search.getLastSearch().id;
            }

            @Override
            public boolean canBeMarked() {
                return SearchService.canBeMarked(currentUserId(), search, hit);
            }

            @Override
            public boolean createdByCurrentUser() {
                return SearchService.createdByUser(currentUserId(), hit);
            }

            @NotNull
            @Override
            public List<BreadCrumb> getMatchingBreadCrumb() {
                return SearchService.getMatchingBreadCrumb(search, hit);
            }
        };
    }

    @Nullable
    private SearchGroup currentSearch() {
        Solutions solutions = controller.service.getSolutions();
        if (solutions == null) return null;
        else return solutions.searchGroup;
    }

    @NotNull
    private Integer currentUserId() {
        return IdeaSamebugPlugin.getInstance().getState().userId;
    }

}
