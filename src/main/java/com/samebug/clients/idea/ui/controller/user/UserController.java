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
package com.samebug.clients.idea.ui.controller.user;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.samebug.clients.idea.components.project.ToolWindowController;
import org.jetbrains.annotations.NotNull;

public class UserController implements Disposable {
    final static Logger LOGGER = Logger.getInstance(UserController.class);
    @NotNull
    final ToolWindowController twc;
    @NotNull
    final Project myProject;

    @NotNull
    final UserStatsController userStatsController;

    public UserController(@NotNull ToolWindowController twc, @NotNull Project project) {
        this.twc = twc;
        this.myProject = project;
        userStatsController = new UserStatsController(this);
    }

    @Override
    public void dispose() {
    }

}
