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
package com.samebug.clients.idea.project.autosearch;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import com.samebug.clients.search.api.LogScanner;
import com.samebug.clients.search.api.LogScannerFactory;
import com.samebug.clients.search.api.StackTraceListener;
import com.samebug.clients.search.matcher.StackTraceMatcher;

public class StackTraceMatcherFactory implements LogScannerFactory {
    private final StackTraceListener listener;
    private final Project project;

    public StackTraceMatcherFactory(Project project) {
        this.project = project;
        this.listener = new PublisherListener(project);
    }

    @Override
    public LogScanner createScanner() {
        return new StackTraceMatcher(listener);
    }


    static class PublisherListener implements StackTraceListener {
        private final Project project;

        PublisherListener(Project project) {
            this.project = project;
        }

        @Override
        public void stacktraceFound(String stacktrace) {
            project.getMessageBus().syncPublisher(StackTraceMatcherListener.FOUND_TOPIC).stackTraceFound(project, stacktrace);
        }
    }

    public interface StackTraceMatcherListener {
        Topic<StackTraceMatcherListener> FOUND_TOPIC = Topic.create("stacktrace found", StackTraceMatcherListener.class, Topic.BroadcastDirection.TO_PARENT);

        void stackTraceFound(Project project, String stackTrace);
    }
}