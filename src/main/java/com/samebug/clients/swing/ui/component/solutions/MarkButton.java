/**
 * Copyright 2017 Samebug, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samebug.clients.swing.ui.component.solutions;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.util.messages.MessageBus;
import com.samebug.clients.common.ui.component.solutions.IMarkButton;
import com.samebug.clients.swing.ui.ColorUtil;
import com.samebug.clients.swing.ui.DrawUtil;
import com.samebug.clients.swing.ui.FontRegistry;
import com.samebug.clients.swing.ui.SamebugBundle;
import com.samebug.clients.swing.ui.component.util.button.SamebugButton;
import com.samebug.clients.swing.ui.component.util.interaction.Colors;
import com.samebug.clients.swing.ui.component.util.label.SamebugLabel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class MarkButton extends SamebugButton implements IMarkButton {
    private Model model;
    private final MessageBus messageBus;

    private final CounterLabel counter;
    private final Separator separator;
    private final MarkLabel markLabel;

    private static final Colors[] ForegroundInteraction = ColorUtil.MarkInteraction;
    private static final Color[] Background = ColorUtil.Background;

    public MarkButton(MessageBus messageBus, Model model) {
        super();
        this.model = new Model(model);
        this.messageBus = messageBus;

        counter = new CounterLabel();
        separator = new Separator();
        markLabel = new MarkLabel();

        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setLayout(new MigLayout("", "12[]9[]10[]8", "8[]8"));

        add(counter, ", h 16!");
        add(separator, "w 1!, h 16!");
        add(markLabel, ", h 16!");

        setFont(FontRegistry.demi(14));
        setForeground(ForegroundInteraction);
        setBackground(Background);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getListener().markClicked(MarkButton.this, getSolutionId(), MarkButton.this.model.userMarkId);
            }
        });

        update(model);
        updateUI();
    }

    public void setLoading() {
        MarkButton.this.setEnabled(false);
    }

    public void update(Model model) {
        MarkButton.this.setEnabled(true);
        this.model = new Model(model);

        if (model.userMarkId == null) setFilled(false);
        else setFilled(true);

        // Cheat. If the button is filled, we use the background color of the button as foreground color of the child components
        Color childrenForeground;
        if (MarkButton.this.isFilled()) childrenForeground = getBackground();
        else childrenForeground = getForeground();
        for (Component c : getComponents()) c.setForeground(childrenForeground);

        counter.update();
        markLabel.update();
    }

    @Override
    protected void paintContent(Graphics2D g2) {
        Font x = markLabel.getFont();
        markLabel.getFontMetrics(x);
        super.paintChildren(g2);
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        // when the button is not filled, we have to change the foreground of the children
        if (!isFilled()) {
            for (Component c : getComponents()) c.setForeground(color);
        }
    }

    private final class CounterLabel extends SamebugLabel {
        {
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(MarkButton.this.getFont());
        }

        void update() {
            setText(Integer.toString(model.marks));
        }
    }

    private final class MarkLabel extends SamebugLabel {
        {
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(MarkButton.this.getFont());
        }

        void update() {
            if (MarkButton.this.model.userMarkId == null) setText(SamebugBundle.message("samebug.component.mark.mark"));
            else setText(SamebugBundle.message("samebug.component.mark.marked"));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
        }
    }

    private final class Separator extends JComponent {
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = DrawUtil.init(g);
            g2.setColor(getForeground());

            g2.drawLine(0, 0, 0, 16);
        }
    }

    private Integer getSolutionId() {
        return DataManager.getInstance().getDataContext(this).getData(SolutionId);
    }

    private Listener getListener() {
        return messageBus.syncPublisher(Listener.TOPIC);
    }

    public static final DataKey<Integer> SolutionId = DataKey.create("SolutionId");
}