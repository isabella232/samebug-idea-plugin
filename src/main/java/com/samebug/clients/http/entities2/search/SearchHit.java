package com.samebug.clients.http.entities2.search;

import com.samebug.clients.http.entities2.mark.Mark;
import com.samebug.clients.http.entities2.mark.Votes;
import com.samebug.clients.http.entities2.solution.Document;
import com.samebug.clients.http.entities2.solution.SolutionSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SearchHit<D extends Document> {
    private SolutionSlot<D> solution;
    private Boolean isMarkable;
    private Mark activeMark;
    private Votes votes;

    @NotNull
    public final SolutionSlot<D> getSolution() {
        return solution;
    }

    @NotNull
    public final Boolean getMarkable() {
        return isMarkable;
    }

    @Nullable
    public final Mark getActiveMark() {
        return activeMark;
    }

    @NotNull
    public final Votes getVotes() {
        return votes;
    }
}
