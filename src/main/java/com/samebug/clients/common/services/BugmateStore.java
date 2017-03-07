/**
 * Copyright 2017 Samebug, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.common.services;

import com.samebug.clients.common.search.api.entities.BugmatesResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BugmateStore {
    final Map<Integer, BugmatesResult> bugmates;

    public BugmateStore() {
        this.bugmates = new ConcurrentHashMap<Integer, BugmatesResult>();
    }

    public BugmatesResult get(int searchId) {
        // TODO def copy
        return bugmates.get(searchId);
    }
}
