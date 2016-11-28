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
package com.samebug.clients.idea.messages.client;

import com.intellij.util.messages.Topic;
import com.samebug.clients.common.search.api.entities.UserInfo;

public interface UserModelListener {
    Topic<UserModelListener> TOPIC = Topic.create("user model changes", UserModelListener.class);

    void successLoadUserInfo(UserInfo result);

    void failLoadUserInfo(Exception e);

    void finishLoadHistory();
}
