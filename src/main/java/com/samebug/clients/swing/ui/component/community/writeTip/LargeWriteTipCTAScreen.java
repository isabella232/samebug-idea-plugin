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
package com.samebug.clients.swing.ui.component.community.writeTip;

import com.samebug.clients.swing.ui.base.button.SamebugButton;
import com.samebug.clients.swing.ui.base.multiline.CenteredMultilineLabel;
import com.samebug.clients.swing.ui.base.panel.EmphasizedPanel;
import com.samebug.clients.swing.ui.modules.MessageService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

final class LargeWriteTipCTAScreen extends EmphasizedPanel implements WriteTipCTAScreen {
    final SamebugButton button;
    final CenteredMultilineLabel label;

    public LargeWriteTipCTAScreen() {
        button = new SamebugButton(MessageService.message("samebug.component.tip.write.cta.button"), true);
        label = new CenteredMultilineLabel();

        setLayout(new MigLayout("fillx, w 300", "40[]40", "40[]20[]40"));
        add(label, "cell 0 0, wmin 0, growx");
        add(button, "cell 0 1, align center");
    }

    @Override
    public JComponent getCTAScreen() {
        return this;
    }

    @Override
    public JComponent getCTAButton() {
        return button;
    }
}
