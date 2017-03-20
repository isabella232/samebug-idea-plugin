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
package com.samebug.clients.swing.ui.component.popup;

import com.samebug.clients.common.ui.component.popup.IHelpRequestPopup;
import com.samebug.clients.swing.ui.base.button.SamebugButton;
import com.samebug.clients.swing.ui.base.label.LinkLabel;
import com.samebug.clients.swing.ui.base.label.SamebugLabel;
import com.samebug.clients.swing.ui.base.multiline.SamebugMultilineLabel;
import com.samebug.clients.swing.ui.base.panel.SamebugPanel;
import com.samebug.clients.swing.ui.component.profile.AvatarIcon;
import com.samebug.clients.swing.ui.modules.ColorService;
import com.samebug.clients.swing.ui.modules.FontService;
import com.samebug.clients.swing.ui.modules.ListenerService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;

public final class HelpRequestPopup extends SamebugPanel implements IHelpRequestPopup {
    private final static int AvatarSize = 40;

    public HelpRequestPopup(Model model) {
        final JComponent avatar = new AvatarIcon(model.avatarUrl, AvatarSize);
        final SamebugLabel title = new SamebugLabel(model.displayName + " sent a tip request", FontService.demi(14));
        final RequestBody body = new RequestBody(model.helpRequestBody);
        final AnswerButton answer = new AnswerButton();
        final JComponent later = new LaterButton();

        setBackgroundColor(ColorService.Tip);
        answer.setBackgroundColor(ColorService.Tip);
        title.setForegroundColor(ColorService.EmphasizedText);
        body.setForegroundColor(ColorService.EmphasizedText);

        setLayout(new MigLayout("", MessageFormat.format("10[{0}!]8[320]10", AvatarSize), "10[]5[]10[]10"));
        add(avatar, "cell 0 0, spany 2, align center top");
        add(title, "cell 1 0");
        add(body, "cell 1 1, growx, wmin 0");
        add(answer, "cell 1 2");
        add(later, "cell 1 2");
    }

    final class RequestBody extends SamebugMultilineLabel {
        public RequestBody(String body) {
            setText(body);
            setFont(FontService.regular(14));
        }
    }

    final class AnswerButton extends SamebugButton {
        {
            setText("Answer");
            setFont(FontService.demi(14));
            setFilled(true);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isEnabled()) getListener().answerClick(HelpRequestPopup.this);
                }
            });
        }
    }

    final class LaterButton extends LinkLabel {
        {
            setText("Later");
            setFont(FontService.demi(14));
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isEnabled()) getListener().laterClick(HelpRequestPopup.this);
                }
            });
        }
    }

    protected Listener getListener() {
        return ListenerService.getListener(this, Listener.class);
    }

}
