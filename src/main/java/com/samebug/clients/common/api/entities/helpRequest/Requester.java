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

import java.net.URL;

public final class Requester {
    @NotNull
    public final Integer id;
    @NotNull
    public final Boolean online;
    @NotNull
    public final String displayName;
    @Nullable
    public final URL avatarUrl;

    public Requester(@NotNull Integer id, @NotNull Boolean online, @NotNull String displayName, @Nullable URL avatarUrl) {
        this.id = id;
        this.online = online;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }
}