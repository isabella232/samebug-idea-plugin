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

import com.samebug.clients.common.api.entities.solution.RestHit;
import com.samebug.clients.common.api.entities.solution.Tip;
import com.samebug.clients.common.api.form.CreateTip;
import com.samebug.clients.common.ui.component.community.IHelpOthersCTA;
import com.samebug.clients.idea.ui.controller.form.CreateTipFormHandler;

final class HelpOthersCTAController implements IHelpOthersCTA.Listener {
    final SolutionFrameController controller;

    public HelpOthersCTAController(final SolutionFrameController controller) {
        this.controller = controller;
    }

    @Override
    public void postTip(final IHelpOthersCTA source, final String tipBody) {
        new CreateTipFormHandler(controller.view, source, new CreateTip(controller.searchId, tipBody, null, null)) {
            @Override
            protected void afterPostForm(RestHit<Tip> response) {
                controller.load();
            }
        }.execute();
    }
}
