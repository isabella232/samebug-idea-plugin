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
package com.samebug.clients.swing.ui.component.profile;

import com.samebug.clients.swing.ui.base.label.SamebugLabel;
import com.samebug.clients.swing.ui.base.panel.TransparentPanel;
import com.samebug.clients.swing.ui.modules.ColorService;
import com.samebug.clients.swing.ui.modules.FontService;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class NumberLabel extends TransparentPanel {
    protected int number;

    protected final SamebugLabel numberLabel;
    protected final SamebugLabel hintLabel;

    public NumberLabel(int number, String hint) {
        this.number = number;
        numberLabel = new SamebugLabel(Integer.toString(number), FontService.demi(14));
        hintLabel = new SamebugLabel(hint, FontService.demi(12));

        setForegroundColor(ColorService.Text);
        setLayout(new MigLayout("fillx", "0[]4px[]0", "0[]0"));

        add(numberLabel, "");
        add(hintLabel, "");
        updateChildrenColor();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        numberLabel.setText(Integer.toString(number));
        revalidate();
        repaint();
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        updateChildrenColor();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        updateChildrenColor();
    }

    @NotNull
    public String getText() {
        return numberLabel.getText() + " " + hintLabel.getText();
    }

    private void updateChildrenColor() {
        for (Component c : getComponents()) c.setForeground(getForeground());
    }
}
