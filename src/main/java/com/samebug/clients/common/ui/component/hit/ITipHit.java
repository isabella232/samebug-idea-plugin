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
package com.samebug.clients.common.ui.component.hit;

import java.net.URL;
import java.util.Date;

public interface ITipHit {
    final class Model {
        public final String message;
        public final int solutionId;
        public final Integer solutionMatchLevel;
        public final String documentId;
        public final Date createdAt;
        public final String createdBy;
        public final URL createdByAvatarUrl;
        public final IMarkButton.Model mark;

        public Model(Model rhs) {
            this(rhs.message, rhs.solutionId, rhs.solutionMatchLevel, rhs.documentId, rhs.createdAt, rhs.createdBy, rhs.createdByAvatarUrl, rhs.mark);
        }

        public Model(String message, int solutionId, Integer solutionMatchLevel, String documentId, Date createdAt, String createdBy, URL createdByAvatarUrl,
                     IMarkButton.Model mark) {
            this.message = message;
            this.solutionId = solutionId;
            this.solutionMatchLevel = solutionMatchLevel;
            this.documentId = documentId;
            this.createdAt = createdAt;
            this.createdBy = createdBy;
            this.createdByAvatarUrl = createdByAvatarUrl;
            this.mark = mark;
        }
    }
}
