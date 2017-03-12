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
package com.samebug.clients.common.ui.component.form;

import com.samebug.clients.common.api.form.FormError;

import java.util.List;

public final class FormMismatchException extends java.lang.Exception {
    public final List<FormError> mismatchedErrors;

    public FormMismatchException(List<FormError> errors) {
        super("Failed to find the following errorKey-errorCode pairs: " + makeString(errors));
        this.mismatchedErrors = errors;
    }

    private static String makeString(List<FormError> errors) {
        StringBuilder b = new StringBuilder();
        for (FormError e : errors) b.append(e).append(", ");
        b.setLength(b.length() - 2);
        return b.toString();
    }
}
