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
package com.samebug.clients.idea.ui.controller.helpRequest;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.samebug.clients.common.api.entities.UserInfo;
import com.samebug.clients.common.api.entities.UserStats;
import com.samebug.clients.common.api.entities.helpRequest.IncomingHelpRequests;
import com.samebug.clients.common.api.entities.helpRequest.MatchingHelpRequest;
import com.samebug.clients.common.api.entities.solution.Solutions;
import com.samebug.clients.common.services.HelpRequestStore;
import com.samebug.clients.common.ui.component.community.IHelpOthersCTA;
import com.samebug.clients.common.ui.component.hit.IWebHit;
import com.samebug.clients.common.ui.component.profile.IProfilePanel;
import com.samebug.clients.common.ui.frame.IFrame;
import com.samebug.clients.common.ui.frame.helpRequest.IHelpRequestFrame;
import com.samebug.clients.common.ui.frame.solution.IWebResultsTab;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.idea.ui.controller.component.ProfileListener;
import com.samebug.clients.idea.ui.controller.component.WebHitListener;
import com.samebug.clients.idea.ui.controller.component.WebResultsTabListener;
import com.samebug.clients.idea.ui.controller.frame.BaseFrameController;
import com.samebug.clients.idea.ui.controller.toolwindow.ToolWindowController;
import com.samebug.clients.swing.ui.frame.helpRequest.HelpRequestFrame;
import com.samebug.clients.swing.ui.modules.ListenerService;

import javax.swing.*;
import java.util.concurrent.Future;

public final class HelpRequestController extends BaseFrameController<IHelpRequestFrame> implements Disposable {
    final String helpRequestId;

    final HelpRequestFrameListener frameListener;
    final ProfileListener profileListener;
    final WriteTipListener writeTipListener;
    final WebHitListener webHitListener;
    WebResultsTabListener webResultsTabListener;

    final HelpRequestStore helpRequestStore;


    public HelpRequestController(ToolWindowController twc, Project project, final String helpRequestId) {
        super(twc, project, new HelpRequestFrame());
        this.helpRequestId = helpRequestId;

        IdeaSamebugPlugin plugin = IdeaSamebugPlugin.getInstance();
        helpRequestStore = plugin.helpRequestStore;

        JComponent frame = (JComponent) view;

        frameListener = new HelpRequestFrameListener(this);
        ListenerService.putListenerToComponent(frame, IFrame.FrameListener.class, frameListener);
        ListenerService.putListenerToComponent(frame, IHelpRequestFrame.Listener.class, frameListener);

        profileListener = new ProfileListener(this);
        ListenerService.putListenerToComponent(frame, IProfilePanel.Listener.class, profileListener);

        writeTipListener = new WriteTipListener(this);
        ListenerService.putListenerToComponent(frame, IHelpOthersCTA.Listener.class, writeTipListener);

        webHitListener = new WebHitListener();
        ListenerService.putListenerToComponent(frame, IWebHit.Listener.class, webHitListener);
    }

    public String getHelpRequestId() {
        return helpRequestId;
    }


    public void load() {
        // TODO other controllers should also make sure to set the loading screen when starting to load content
        view.setLoading();
        webResultsTabListener = null;
        ListenerService.putListenerToComponent((JComponent) view, IWebResultsTab.Listener.class, null);

        final Future<UserInfo> userInfoTask = concurrencyService.userInfo();
        final Future<UserStats> userStatsTask = concurrencyService.userStats();
        final Future<IncomingHelpRequests> incomingHelpRequestsTask = concurrencyService.incomingHelpRequests(false);
        final Future<MatchingHelpRequest> helpRequestTask = concurrencyService.helpRequest(helpRequestId);

        load(helpRequestTask, incomingHelpRequestsTask, userInfoTask, userStatsTask);
    }

    /**
     * Wait for the help request so we can decide which search id to use for showing the solutions
     */
    private void load(final Future<MatchingHelpRequest> helpRequestTask,
                      final Future<IncomingHelpRequests> incomingHelpRequestsTask,
                      final Future<UserInfo> userInfoTask,
                      final Future<UserStats> userStatsTask) {
        new LoadingTask() {
            @Override
            protected void load() throws Exception {
                MatchingHelpRequest helpRequest = helpRequestTask.get();
                int accessibleSearchId = helpRequest.accessibleSearchInfo().id;

                webResultsTabListener = new WebResultsTabListener(accessibleSearchId);
                ListenerService.putListenerToComponent((JComponent) view, IWebResultsTab.Listener.class, webResultsTabListener);

                final Future<Solutions> solutionsTask = concurrencyService.solutions(accessibleSearchId);
                HelpRequestController.this.load(solutionsTask, helpRequestTask, incomingHelpRequestsTask, userInfoTask, userStatsTask);
            }
        }.executeInBackground();

    }

    private void load(final Future<Solutions> solutionsTask,
                      final Future<MatchingHelpRequest> helpRequestTask,
                      final Future<IncomingHelpRequests> incomingHelpRequestsTask,
                      final Future<UserInfo> userInfoTask,
                      final Future<UserStats> userStatsTask) {
        new LoadingTask() {
            @Override
            protected void load() throws Exception {
                final IHelpRequestFrame.Model model = conversionService.convertHelpRequestFrame(
                        solutionsTask.get(), helpRequestTask.get(), incomingHelpRequestsTask.get(), userInfoTask.get(), userStatsTask.get());
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        view.loadingSucceeded(model);
                    }
                });
            }
        }.executeInBackground();
    }
}