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
package com.samebug.clients.idea.ui.controller.toolwindow;

import com.intellij.util.messages.MessageBusConnection;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.idea.ui.modules.BrowserUtil;

import java.net.URI;

public class OpenCrashListener implements com.samebug.clients.idea.messages.OpenCrashListener {
    final ToolWindowController twc;

    public OpenCrashListener(ToolWindowController twc) {
        this.twc = twc;
        final MessageBusConnection connection = twc.project.getMessageBus().connect(twc);
        connection.subscribe(OpenCrashListener.TOPIC, this);
    }

    @Override
    public void openCrash(int searchId) {
        URI searchUrl = IdeaSamebugPlugin.getInstance().uriBuilder.search(searchId);
        BrowserUtil.browse(searchUrl);
    }
}
