/**
 * Copyright 2017 Samebug, Inc.
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
package com.samebug.clients.idea.ui.controller.solution;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.messages.MessageBusConnection;
import com.samebug.clients.common.ui.component.solutions.IHelpOthersCTA;

final class HelpOthersCTAController implements IHelpOthersCTA.Listener {
    final static Logger LOGGER = Logger.getInstance(HelpOthersCTAController.class);
    final SolutionsController controller;

    public HelpOthersCTAController(final SolutionsController controller) {
        this.controller = controller;

        MessageBusConnection projectConnection = controller.myProject.getMessageBus().connect(controller);
        projectConnection.subscribe(IHelpOthersCTA.Listener.TOPIC, this);
    }

    @Override
    public void ctaClicked(IHelpOthersCTA source) {
        LOGGER.debug("help others cta clicked");
    }
}