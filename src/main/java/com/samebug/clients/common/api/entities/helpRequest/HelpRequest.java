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
package com.samebug.clients.common.api.entities.helpRequest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public final class HelpRequest {
    @NotNull
    public final String id;
    @NotNull
    public final Requester requester;
    @NotNull
    public final Integer workspaceId;
    @NotNull
    public final String context;
    // TODO enum
    @NotNull
    public final String visibility;
    @NotNull
    public final Date createdAt;
    @Nullable
    public final Date revokedAt;
    @Nullable
    public final Date viewedAt;

    public HelpRequest(@NotNull String id,
                       @NotNull Requester requester,
                       @NotNull Integer workspaceId,
                       @NotNull String context,
                       @NotNull String visibility,
                       @NotNull Date createdAt,
                       @Nullable Date revokedAt,
                       @Nullable Date viewedAt) {
        this.id = id;
        this.requester = requester;
        this.workspaceId = workspaceId;
        this.context = context;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.revokedAt = revokedAt;
        this.viewedAt = viewedAt;
    }
}
