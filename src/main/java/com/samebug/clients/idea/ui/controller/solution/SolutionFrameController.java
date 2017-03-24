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
package com.samebug.clients.idea.ui.controller.solution;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import com.samebug.clients.common.api.entities.bugmate.BugmatesResult;
import com.samebug.clients.common.api.entities.helpRequest.IncomingHelpRequests;
import com.samebug.clients.common.api.entities.profile.UserInfo;
import com.samebug.clients.common.api.entities.profile.UserStats;
import com.samebug.clients.common.api.entities.search.SearchDetails;
import com.samebug.clients.common.api.entities.solution.Solutions;
import com.samebug.clients.common.ui.component.community.IAskForHelp;
import com.samebug.clients.common.ui.component.community.IHelpOthersCTA;
import com.samebug.clients.common.ui.component.helpRequest.IMyHelpRequest;
import com.samebug.clients.common.ui.component.hit.IMarkButton;
import com.samebug.clients.common.ui.component.hit.IWebHit;
import com.samebug.clients.common.ui.component.profile.IProfilePanel;
import com.samebug.clients.common.ui.frame.IFrame;
import com.samebug.clients.common.ui.frame.solution.ISearchHeaderPanel;
import com.samebug.clients.common.ui.frame.solution.ISolutionFrame;
import com.samebug.clients.common.ui.frame.solution.IWebResultsTab;
import com.samebug.clients.idea.messages.IncomingHelpRequest;
import com.samebug.clients.idea.messages.RefreshTimestampsListener;
import com.samebug.clients.idea.ui.controller.component.ProfileListener;
import com.samebug.clients.idea.ui.controller.component.WebHitListener;
import com.samebug.clients.idea.ui.controller.component.WebResultsTabListener;
import com.samebug.clients.idea.ui.controller.externalEvent.ProfileUpdateListener;
import com.samebug.clients.idea.ui.controller.externalEvent.RefreshListener;
import com.samebug.clients.idea.ui.controller.frame.BaseFrameController;
import com.samebug.clients.idea.ui.controller.toolwindow.ToolWindowController;
import com.samebug.clients.swing.ui.frame.solution.SolutionFrame;
import com.samebug.clients.swing.ui.modules.ListenerService;

import javax.swing.*;
import java.util.concurrent.Future;

public final class SolutionFrameController extends BaseFrameController<ISolutionFrame> implements Disposable {
    final int searchId;

    final RefreshListener refreshListener;
    final ProfileUpdateListener profileUpdateListener;
    final ExceptionHeaderListener exceptionHeaderListener;
    final WebResultsTabListener webResultsTabListener;
    final RequestHelpListener requestHelpListener;
    final RevokeHelpRequestListener revokeHelpRequestListener;
    final HelpOthersCTAListener helpOthersCTAListener;
    final WebHitListener webHitListener;
    final MarkButtonListener markButtonListener;
    final ProfileListener profileListener;
    final SolutionFrameListener frameListener;

    public SolutionFrameController(ToolWindowController twc, Project project, final int searchId) {
        super(twc, project, new SolutionFrame());
        this.searchId = searchId;

        // bind view listeners
        JComponent frame = (JComponent) view;
        frameListener = new SolutionFrameListener(this);
        ListenerService.putListenerToComponent(frame, IFrame.FrameListener.class, frameListener);
        ListenerService.putListenerToComponent(frame, ISolutionFrame.Listener.class, frameListener);

        exceptionHeaderListener = new ExceptionHeaderListener(this);
        ListenerService.putListenerToComponent(frame, ISearchHeaderPanel.Listener.class, exceptionHeaderListener);

        webResultsTabListener = new WebResultsTabListener(searchId);
        ListenerService.putListenerToComponent(frame, IWebResultsTab.Listener.class, webResultsTabListener);

        webHitListener = new WebHitListener();
        ListenerService.putListenerToComponent(frame, IWebHit.Listener.class, webHitListener);

        requestHelpListener = new RequestHelpListener(this);
        ListenerService.putListenerToComponent(frame, IAskForHelp.Listener.class, requestHelpListener);

        revokeHelpRequestListener = new RevokeHelpRequestListener(this);
        ListenerService.putListenerToComponent(frame, IMyHelpRequest.Listener.class, revokeHelpRequestListener);

        helpOthersCTAListener = new HelpOthersCTAListener(this);
        ListenerService.putListenerToComponent(frame, IHelpOthersCTA.Listener.class, helpOthersCTAListener);

        profileListener = new ProfileListener(this);
        ListenerService.putListenerToComponent(frame, IProfilePanel.Listener.class, profileListener);

        markButtonListener = new MarkButtonListener(this);
        ListenerService.putListenerToComponent(frame, IMarkButton.Listener.class, markButtonListener);

        // bind external listeners
        MessageBusConnection projectConnection = myProject.getMessageBus().connect(this);
        refreshListener = new RefreshListener(this);
        projectConnection.subscribe(RefreshTimestampsListener.TOPIC, refreshListener);

        profileUpdateListener = new ProfileUpdateListener(this);
        projectConnection.subscribe(IncomingHelpRequest.TOPIC, profileUpdateListener);

    }

    public int getSearchId() {
        return searchId;
    }

    public void load() {
        view.setLoading();
        final Future<UserInfo> userInfoTask = concurrencyService.userInfo();
        final Future<UserStats> userStatsTask = concurrencyService.userStats();
        final Future<IncomingHelpRequests> incomingHelpRequestsTask = concurrencyService.incomingHelpRequests(false);
        final Future<Solutions> solutionsTask = concurrencyService.solutions(searchId);
        final Future<BugmatesResult> bugmatesTask = concurrencyService.bugmates(searchId);
        final Future<SearchDetails> searchTask = concurrencyService.search(searchId);
        load(solutionsTask, bugmatesTask, searchTask, incomingHelpRequestsTask, userInfoTask, userStatsTask);
    }

    private void load(final Future<Solutions> solutionsTask,
                      final Future<BugmatesResult> bugmatesTask,
                      final Future<SearchDetails> searchTask,
                      final Future<IncomingHelpRequests> incomingHelpRequestsTask,
                      final Future<UserInfo> userInfoTask,
                      final Future<UserStats> userStatsTask) {
        new LoadingTask() {
            @Override
            protected void load() throws Exception {
                final SolutionFrame.Model model =
                        conversionService.solutionFrame(searchTask.get(), solutionsTask.get(), bugmatesTask.get(), incomingHelpRequestsTask.get(),
                                userInfoTask.get(), userStatsTask.get());
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

