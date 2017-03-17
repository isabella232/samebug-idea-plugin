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
package com.samebug.clients.idea.ui.controller.toolwindow;

import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.util.messages.MessageBusConnection;
import com.samebug.clients.common.api.entities.helpRequest.HelpRequest;
import com.samebug.clients.common.ui.component.popup.IHelpRequestPopup;
import com.samebug.clients.idea.notifications.IncomingHelpRequestNotification;
import com.samebug.clients.idea.ui.modules.IdeaListenerService;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class HelpRequestPopupListener implements IHelpRequestPopup.Listener {
    final ToolWindowController twc;
    final Map<IHelpRequestPopup, HelpRequest> data;
    final Map<IHelpRequestPopup, IncomingHelpRequestNotification> notifications;
    final Map<IHelpRequestPopup, Balloon> balloons;

    public HelpRequestPopupListener(ToolWindowController twc) {
        this.twc = twc;
        data = new HashMap<IHelpRequestPopup, HelpRequest>();
        notifications = new HashMap<IHelpRequestPopup, IncomingHelpRequestNotification>();
        balloons = new HashMap<IHelpRequestPopup, Balloon>();

        MessageBusConnection projectConnection = twc.project.getMessageBus().connect(twc);
        projectConnection.subscribe(IdeaListenerService.HelpRequestPopup, this);
    }

    @Override
    public void answerClick(IHelpRequestPopup source) {
        HelpRequest helpRequest = data.get(source);
        assert helpRequest != null;
        twc.focusOnHelpRequest(helpRequest.id);

        hideAndRemoveIncomingHelpRequest(source);
    }

    @Override
    public void laterClick(IHelpRequestPopup source) {
        hideAndRemoveIncomingHelpRequest(source);
    }

    public void addIncomingHelpRequest(@NotNull HelpRequest helpRequest,
                                       @NotNull IncomingHelpRequestNotification notification,
                                       @NotNull Balloon balloon,
                                       @NotNull IHelpRequestPopup view) {
        data.put(view, helpRequest);
        notifications.put(view, notification);
        balloons.put(view, balloon);
    }

    private void hideAndRemoveIncomingHelpRequest(@NotNull IHelpRequestPopup view) {
        Balloon balloon = balloons.get(view);
        IncomingHelpRequestNotification notification = notifications.get(view);

        assert balloon != null;
        assert notification != null;
        notification.expire();
        balloon.hide();

        balloons.remove(view);
        notifications.remove(view);
        data.remove(view);
    }
}
