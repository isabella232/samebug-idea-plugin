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
package com.samebug.clients.swing.ui.component.bugmate;

import com.samebug.clients.common.ui.component.community.IAskForHelp;
import com.samebug.clients.common.ui.modules.MessageService;
import com.samebug.clients.common.ui.modules.TrackingService;
import com.samebug.clients.swing.tracking.SwingRawEvent;
import com.samebug.clients.swing.tracking.TrackingKeys;
import com.samebug.clients.swing.ui.base.button.SamebugButton;
import com.samebug.clients.swing.ui.base.label.LinkLabel;
import com.samebug.clients.swing.ui.modules.DataService;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class RequestHelpScreen extends JComponent {
    final WriteRequestArea writeRequestArea;
    final SamebugButton sendButton;
    final LinkLabel cancelButton;

    public RequestHelpScreen(final RequestHelp requestHelp) {
        writeRequestArea = new WriteRequestArea(requestHelp);
        sendButton = new SendButton();
        cancelButton = new LinkLabel(MessageService.message("samebug.component.helpRequest.ask.cancel"));
        DataService.putData(cancelButton, TrackingKeys.Label, cancelButton.getText());

        setLayout(new MigLayout("fillx", "0[]0", "0[]10px[]10px[]0"));
        add(writeRequestArea, "cell 0 0, growx");
        add(sendButton, "cell 0 1, align center");
        add(cancelButton, "cell 0 2, align center");

        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEnabled()) {
                    requestHelp.getListener().askBugmates(requestHelp, writeRequestArea.getText());
                }
            }
        });

        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEnabled()) {
                    requestHelp.changeToClosedState();
                    TrackingService.trace(SwingRawEvent.buttonClick(cancelButton));
                }
            }
        });
    }

    public void setFormErrors(@NotNull final IAskForHelp.BadRequest errors) {
        if (errors.context != null) writeRequestArea.setFormError(errors.context);
        revalidate();
        repaint();
    }

    private final class SendButton extends SamebugButton {
        SendButton() {
            super(MessageService.message("samebug.component.helpRequest.ask.send"), true);
            DataService.putData(this, TrackingKeys.Label, getText());
        }
    }
}
