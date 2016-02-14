/**
 * Copyright 2016 Samebug, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.idea.components.application;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.samebug.clients.idea.notification.SamebugNotification;
import com.samebug.clients.idea.ui.SettingsDialog;
import com.samebug.clients.search.api.SamebugClient;
import com.samebug.clients.search.api.entities.UserInfo;
import com.samebug.clients.search.api.exceptions.SamebugClientException;
import com.samebug.clients.search.api.exceptions.UnknownApiKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(
        name = "SamebugConfiguration",
        storages = {
                @Storage(id = "SamebugClient", file = "$APP_CONFIG$/SamebugClient.xml")
        }
)
final public class IdeaSamebugPlugin implements ApplicationComponent, PersistentStateComponent<Settings> {
    private SamebugClient client = new SamebugClient(null);

    // TODO Unlike other methods, this one executes the http request on the caller thread. Is it ok?
    public void setApiKey(@NotNull String apiKey) throws SamebugClientException, UnknownApiKey {
        UserInfo userInfo = client.getUserInfo(apiKey);
        client = new SamebugClient(apiKey);
        state.setApiKey(apiKey);
        state.setUserId(userInfo.userId);
        state.setUserDisplayName(userInfo.displayName);
    }

    @Nullable
    public String getApiKey() {
        return state.getApiKey();
    }

    @NotNull
    public static IdeaSamebugPlugin getInstance() {
        IdeaSamebugPlugin instance = ApplicationManager.getApplication().getComponent(IdeaSamebugPlugin.class);
        if (instance == null) {
            throw new Error("No Samebug IDEA plugin available");
        } else {
            return instance;
        }
    }

    public SamebugClient getClient() {
        return client;
    }

    // ApplicationComponent overrides
    @Override
    final public void initComponent() {
        SamebugNotification.registerNotificationGroups();
        if (!state.isInitialized()) ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                SettingsDialog.setup(state.getApiKey());
            }
        });
    }

    @Override
    final public void disposeComponent() {
    }

    @Override
    @NotNull
    final public String getComponentName() {
        return getClass().getSimpleName();
    }


    // PersistentStateComponent overrides
    @Nullable
    @Override
    public Settings getState() {
        return this.state;
    }

    @Override
    public void loadState(Settings state) {
        this.state = state;
        client = new SamebugClient(state.getApiKey());
    }

    private Settings state = new Settings();
}
