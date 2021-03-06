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
package com.samebug.clients.idea.ui.controller.welcome;

import com.samebug.clients.common.ui.frame.welcome.IWelcomeFrame;
import com.samebug.clients.idea.ui.controller.frame.BaseFrameListener;

public final class WelcomeFrameListener extends BaseFrameListener implements IWelcomeFrame.Listener {
    final WelcomeController controller;

    public WelcomeFrameListener(final WelcomeController controller) {
        super(controller.myProject);
        this.controller = controller;
    }

    @Override
    public void reload() {
        controller.load();
    }
}
