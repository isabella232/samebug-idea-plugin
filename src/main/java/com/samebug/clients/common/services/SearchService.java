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
package com.samebug.clients.common.services;

import com.samebug.clients.http.client.SamebugClient;
import com.samebug.clients.http.entities.response.SearchRequest;
import com.samebug.clients.http.exceptions.SamebugClientException;
import com.samebug.clients.http.form.SearchCreate;

public final class SearchService {
    final ClientService clientService;
    final SearchStore store;

    public SearchService(ClientService clientService, SearchStore store) {
        this.clientService = clientService;
        this.store = store;
    }

    public SearchRequest search(final String trace) throws SamebugClientException {
        final SamebugClient client = clientService.client;
        return client.createSearch(new SearchCreate(trace));
    }

    public SearchRequest get(final int searchId) throws SamebugClientException {
        final SamebugClient client = clientService.client;
        try {
            SearchRequest result = client.getSearch(searchId);
            store.searches.put(searchId, result);
            return result;
        } catch (SamebugClientException e) {
            store.searches.remove(searchId);
            throw e;
        }
    }


}
