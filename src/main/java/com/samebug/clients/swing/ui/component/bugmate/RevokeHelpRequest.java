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
package com.samebug.clients.swing.ui.component.bugmate;

import com.samebug.clients.common.api.entities.helpRequest.MyHelpRequest;
import com.samebug.clients.common.ui.modules.TextService;
import com.samebug.clients.swing.ui.base.button.SamebugButton;
import com.samebug.clients.swing.ui.base.label.SamebugLabel;
import com.samebug.clients.swing.ui.base.panel.SamebugPanel;
import com.samebug.clients.swing.ui.modules.ColorService;
import com.samebug.clients.swing.ui.modules.FontService;
import com.samebug.clients.swing.ui.modules.MessageService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class RevokeHelpRequest extends JComponent {
    final BugmateList bugmateList;

    private final JComponent helpRequestLabel;
    private final SamebugButton revoke;

    public RevokeHelpRequest(BugmateList bugmateList) {
        this.bugmateList = bugmateList;
        helpRequestLabel = new HelpRequestLabel(bugmateList.model.helpRequest);
        revoke = new RevokeButton(bugmateList.model.helpRequest);

        setLayout(new MigLayout("fillx", "0[]0", "0[]15[]0"));
        add(helpRequestLabel, "cell 0 0, growx");
        add(revoke, "cell 0 1, align center");
    }

    public void startRevoke() {
        revoke.changeToLoadingAnimation();
    }

    public void failRevoke() {
        revoke.revertFromLoadingAnimation();
    }

    public void successRevoke() {
        revoke.revertFromLoadingAnimation();
    }

    private final class HelpRequestLabel extends SamebugPanel {
        public HelpRequestLabel(MyHelpRequest helpRequest) {
            final SamebugLabel text = new SamebugLabel(format(helpRequest));
            text.setFont(FontService.regular(16));
            text.setForegroundColor(ColorService.TipText);
            text.setHorizontalAlignment(SwingConstants.CENTER);
            setBackgroundColor(ColorService.Tip);

            setLayout(new MigLayout("fillx", "30[fill]30", "30[]30"));
            add(text, "al center");
        }

        String format(MyHelpRequest r) {
            // TODO it is not exactly the same as in the design
            String date = TextService.adaptiveTimestamp(r.createdAt);
            return MessageService.message("samebug.component.helpRequest.ask.alreadySent", date);
        }
    }

    private final class RevokeButton extends SamebugButton {
        public RevokeButton(final MyHelpRequest helpRequest) {
            setText(MessageService.message("samebug.component.helpRequest.ask.revoke"));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isEnabled()) bugmateList.getListener().revokeHelpRequest(bugmateList, helpRequest.id);
                }
            });
        }
    }
}
