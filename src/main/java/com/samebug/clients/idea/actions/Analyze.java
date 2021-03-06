/*
 * Copyright 2018 Samebug, Inc.
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
package com.samebug.clients.idea.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.samebug.clients.common.tracking.Funnels;
import com.samebug.clients.common.tracking.Hooks;
import com.samebug.clients.common.ui.modules.TrackingService;
import com.samebug.clients.idea.ui.dialog.analyze.AnalyzeDialog;
import com.samebug.clients.swing.tracking.SwingRawEvent;

public class Analyze extends AnAction implements DumbAware {
    @Override
    public void actionPerformed(AnActionEvent e) {
        final String transactionId = Funnels.newTransactionId();
        TrackingService.trace(SwingRawEvent.searchHookTrigger(transactionId, Hooks.Search.MENU));
        AnalyzeDialog dialog = new AnalyzeDialog(e.getProject(), transactionId);
        dialog.show();
    }
}
