/*
 * Copyright 2017 Samebug, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.common.search.api.json;

import com.google.common.collect.ImmutableMap;
import com.samebug.clients.common.search.api.entities.Search;
import com.samebug.clients.common.search.api.entities.StackTraceSearch;
import com.samebug.clients.common.search.api.entities.TextSearch;

final class SearchAdapter extends AbstractObjectAdapter<Search> {
    {
        typeClasses = ImmutableMap.<String, Class<? extends Search>>builder()
                .put("stacktrace", StackTraceSearch.class)
                .put("text", TextSearch.class)
                .build();
    }
}
