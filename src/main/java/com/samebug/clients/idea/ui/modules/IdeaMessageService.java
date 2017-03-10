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
package com.samebug.clients.idea.ui.modules;

import com.intellij.AbstractBundle;
import com.samebug.clients.swing.ui.modules.MessageService;

public final class IdeaMessageService extends MessageService {
    private static final MessageBundle BUNDLE = new MessageBundle();

    public String internalMessage(String key, Object... params) {
        return BUNDLE.getMessage(key, params);
    }


    private static final class MessageBundle extends AbstractBundle {
        MessageBundle() {
            super(PATH_TO_BUNDLE);
        }
    }
}

