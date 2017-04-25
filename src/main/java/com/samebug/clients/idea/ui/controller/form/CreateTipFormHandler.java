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
package com.samebug.clients.idea.ui.controller.form;

import com.samebug.clients.common.services.SolutionService;
import com.samebug.clients.common.ui.component.community.IHelpOthersCTA;
import com.samebug.clients.common.ui.frame.IFrame;
import com.samebug.clients.http.entities.solution.SamebugTip;
import com.samebug.clients.http.entities.solution.SolutionSlot;
import com.samebug.clients.http.exceptions.SamebugClientException;
import com.samebug.clients.http.form.TipCreate;
import com.samebug.clients.idea.components.application.IdeaSamebugPlugin;
import com.samebug.clients.swing.ui.modules.MessageService;

public abstract class CreateTipFormHandler extends PostFormHandler<SolutionSlot<SamebugTip>, TipCreate.BadRequest> {
    final IFrame frame;
    final IHelpOthersCTA form;
    final TipCreate.Base data;

    public CreateTipFormHandler(IFrame frame, IHelpOthersCTA form, TipCreate.Base data) {
        this.frame = frame;
        this.form = form;
        this.data = data;
    }

    @Override
    protected void beforePostForm() {
        form.startPostTip();
    }

    @Override
    protected SolutionSlot<SamebugTip> postForm() throws SamebugClientException, TipCreate.BadRequest {
        final SolutionService solutionService = IdeaSamebugPlugin.getInstance().solutionService;
        return solutionService.postTip(data);
    }

    @Override
    protected void handleBadRequest(TipCreate.BadRequest fieldErrors) {
//        if (CreateTip.BODY.equals(fieldError.key)) fieldErrors.add(fieldError);
//        if (nonFormError.code.equals(CreateTip.E_TOO_SHORT)) fieldErrors.add(new FieldError(CreateTip.BODY, CreateTip.E_TOO_SHORT));
//        else if (nonFormError.code.equals(CreateTip.E_TOO_LONG)) fieldErrors.add(new FieldError(CreateTip.BODY, CreateTip.E_TOO_LONG));
        IHelpOthersCTA.BadRequest b = null;
        form.failPostTipWithFormError(b);
    }

    @Override
    protected void handleOtherClientExceptions(SamebugClientException exception) {
        frame.popupError(MessageService.message("samebug.component.tip.write.error.unhandled"));
        form.failPostTipWithFormError(null);
    }
}
