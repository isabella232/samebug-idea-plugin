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
package com.samebug.clients.http.form;

import com.samebug.clients.http.exceptions.FormException;
import com.samebug.clients.http.exceptions.SamebugException;
import org.jetbrains.annotations.NotNull;

public final class CancelMark {
    public static final String MARK = "mark";

    public static final String E_NOT_YOUR_MARK = "NOT_YOUR_MARK";
    public static final String E_ALREADY_CANCELLED = "ALREADY_CANCELLED";

    @NotNull
    public final Integer markId;

    public CancelMark(@NotNull Integer markId) {
        this.markId = markId;
    }

    public static final class Error {}
    public static final class BadRequest extends FormException {
        public final Error error;

        public BadRequest(Error error) {
            this.error = error;
        }

    }
}
