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

import com.samebug.clients.http.entities.jsonapi.JsonErrors;
import com.samebug.clients.http.exceptions.FormException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public final class MarkCreate {
    public static final String SOLUTION = "solution";
    public static final String SEARCH = "search";

    public static final String E_ALREADY_MARKED = "ALREADY_MARKED";
    public static final String E_NOT_YOUR_SEARCH = "NOT_YOUR_SEARCH";

    public static final class Data {
        public final String type = "mark-searchhit";
        public final Integer solutionId;
        public final Integer searchId;

        public Data(@NotNull Integer solutionId, @NotNull Integer searchId) {
            this.solutionId = solutionId;
            this.searchId = searchId;
        }
    }

    public enum ErrorCode {
        TOO_SHORT
    }

    public static class BadRequest extends FormException {
        public final JsonErrors<ErrorCode> errorList;

        public BadRequest(JsonErrors<ErrorCode> errorList) {
            this.errorList = errorList;
        }

        public String toString() {
            return super.toString() + ": " + StringUtils.join(errorList.getErrorCodes(), ", ");
        }
    }
}
