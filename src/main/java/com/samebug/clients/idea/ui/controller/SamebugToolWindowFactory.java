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
package com.samebug.clients.idea.ui.controller;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.messages.MessageBusConnection;
import com.samebug.clients.idea.components.application.IdeaClientService;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.idea.messages.ConnectionStatusListener;
import com.samebug.clients.idea.resources.SamebugBundle;
import com.samebug.clients.search.api.entities.GroupedHistory;
import com.samebug.clients.search.api.exceptions.SamebugClientException;
import org.jetbrains.annotations.NotNull;


final public class SamebugToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        HistoryTabController historyTab = initializeHistoryTab(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(historyTab.getControlPanel(), SamebugBundle.message("samebug.toolwindow.history.tabName"), false);
        toolWindow.getContentManager().addContent(content);
    }

    HistoryTabController initializeHistoryTab(final Project project) {
        final HistoryTabController historyTab = ServiceManager.getService(project, HistoryTabController.class);

        MessageBusConnection appMessageBus = ApplicationManager.getApplication().getMessageBus().connect(project);
        appMessageBus.subscribe(ConnectionStatusListener.CONNECTION_STATUS_TOPIC, historyTab.getStatusUpdater());

        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                IdeaClientService client = IdeaSamebugPlugin.getInstance().getClient();
                try {
                    final GroupedHistory history = client.getSearchHistory();
                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            historyTab.update(history);
                        }
                    });
                } catch (SamebugClientException e1) {
                    historyTab.update(null);
                }
            }
        });

        return historyTab;
    }
}
