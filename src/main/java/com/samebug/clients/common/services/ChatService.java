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

import com.samebug.clients.http.entities.chat.ChatRoom;
import com.samebug.clients.http.entities.helprequest.NewChatRoom;
import com.samebug.clients.http.exceptions.SamebugClientException;
import com.samebug.clients.http.exceptions.UnsuccessfulResponseStatus;
import com.samebug.clients.http.form.CreateChatRoom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ChatService {
    @NotNull
    final ClientService clientService;

    public ChatService(@NotNull final ClientService clientService) {
        this.clientService = clientService;
    }

    @NotNull
    public ChatRoom createChatRoom(@NotNull final Integer searchId, @NotNull final NewChatRoom data) throws SamebugClientException, CreateChatRoom.BadRequest {
        return clientService.getClient().createNewChat(searchId, data);
    }

    @Nullable
    public ChatRoom chatRoom(@NotNull final Integer searchId) throws SamebugClientException {
        try {
            return clientService.getClient().getChatRoom(searchId);
        } catch (UnsuccessfulResponseStatus x) {
            if (x.statusCode == 404) return null;
            else throw x;
        }
    }
}