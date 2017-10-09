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
package com.samebug.clients.idea.ui.controller.popupController;

import com.samebug.clients.common.ui.component.popup.IIncomingChatInvitationPopup;
import com.samebug.clients.http.entities.notification.ChatInvitation;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.idea.ui.modules.BrowserUtil;
import com.samebug.clients.swing.ui.component.popup.IncomingChatInvitationPopup;

import java.net.URI;

public final class IncomingChatInvitationPopupListener implements IncomingChatInvitationPopup.Listener {
    final IncomingChatInvitationPopupController controller;
    private final ChatInvitation chatInvitation;

    public IncomingChatInvitationPopupListener(IncomingChatInvitationPopupController controller, ChatInvitation chatInvitation) {
        this.controller = controller;
        this.chatInvitation = chatInvitation;
    }

    @Override
    public void openChat(IIncomingChatInvitationPopup source) {
        Integer searchId = chatInvitation.getInvitationSource().getSearch().getId();
        URI searchUrl = IdeaSamebugPlugin.getInstance().uriBuilder.search(searchId);
        BrowserUtil.browse(searchUrl);
    }
}