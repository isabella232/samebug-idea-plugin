/**
 * Copyright 2016 Samebug, Inc.
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
package com.samebug.clients.search.api.entities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class StackTraceWithBreadCrumbs {
    @NotNull
    public String stackTraceId;
    @NotNull
    public Exception trace;
    @NotNull
    public List<BreadCrumb> breadCrumbs;

    public StackTraceWithBreadCrumbs(@NotNull final StackTraceWithBreadCrumbs rhs) {
        this.stackTraceId = rhs.stackTraceId;
        this.trace = new Exception(rhs.trace);
        this.breadCrumbs = new ArrayList<BreadCrumb>(rhs.breadCrumbs.size());
        for (BreadCrumb c : rhs.breadCrumbs) {
            this.breadCrumbs.add(new BreadCrumb(c));
        }
    }
}