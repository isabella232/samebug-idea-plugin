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

import com.intellij.util.messages.MessageBusConnection;
import com.samebug.clients.common.ui.frame.solution.ISolutionFrame;
import com.samebug.clients.idea.ui.controller.frame.BaseFrameListener;
import com.samebug.clients.idea.ui.modules.IdeaListenerService;

final class SolutionFrameController extends BaseFrameListener implements ISolutionFrame.Listener {
    final SolutionsController controller;

    public SolutionFrameController(final SolutionsController controller) {
        super(controller.myProject);
        this.controller = controller;

        MessageBusConnection projectConnection = controller.myProject.getMessageBus().connect(controller);
        projectConnection.subscribe(IdeaListenerService.SolutionFrame, this);
    }

    @Override
    public void reload() {
        controller.view.setLoading();
        controller.load();
    }
}
