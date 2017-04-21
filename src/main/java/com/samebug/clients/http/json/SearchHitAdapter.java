package com.samebug.clients.http.json;

import com.google.common.collect.ImmutableMap;
import com.samebug.clients.http.entities2.search.SearchHit;
import com.samebug.clients.http.entities2.search.StackTraceSearchHit;
import com.samebug.clients.http.entities2.search.TextSearchHit;

public class SearchHitAdapter extends AbstractObjectAdapter<SearchHit> {
    {
        typeClasses = ImmutableMap.<String, Class<? extends SearchHit>>builder()
                .put("search-text-hit", TextSearchHit.class)
                .put("search-stacktrace-hit", StackTraceSearchHit.class)
                .build();
    }
}
