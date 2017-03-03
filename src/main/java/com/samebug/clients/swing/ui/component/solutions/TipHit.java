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

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.util.messages.MessageBus;
import com.samebug.clients.common.ui.TextUtil;
import com.samebug.clients.common.ui.component.solutions.ITipHit;
import com.samebug.clients.swing.ui.ColorUtil;
import com.samebug.clients.swing.ui.DrawUtil;
import com.samebug.clients.swing.ui.FontRegistry;
import com.samebug.clients.swing.ui.component.util.AvatarIcon;
import com.samebug.clients.swing.ui.component.util.label.SamebugLabel;
import com.samebug.clients.swing.ui.component.util.multiline.SamebugMultilineLabel;
import com.samebug.clients.swing.ui.component.util.panel.TransparentPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public final class TipHit extends TransparentPanel implements ITipHit, DataProvider {
    private final Model model;
    private final MessageBus messageBus;

    private final SamebugLabel tipLabel;
    private final MessageLabel tipMessage;
    private final MarkButton mark;

    public TipHit(MessageBus messageBus, Model model) {
        this.model = new Model(model);
        this.messageBus = messageBus;

        setBackground(ColorUtil.Tip);
        tipLabel = new SamebugLabel("TIP", FontRegistry.regular(14));
        tipLabel.setForeground(ColorUtil.TipText);
        tipMessage = new MessageLabel();
        mark = new MarkButton(messageBus, model.mark);
        final JPanel filler = new TransparentPanel();
        final AuthorPanel author = new AuthorPanel();

        setLayout(new MigLayout("fillx", "20[fill, 300]20", "20[]15[]15[]20"));

        add(tipLabel, "cell 0 0");
        add(tipMessage, "cell 0 1, wmin 0, growx");
        add(mark, "cell 0 2, align left");
        add(filler, "cell 0 2, growx");
        add(author, "cell 0 2, align right");
    }

    @Override
    public void paintBorder(Graphics g) {
        Graphics2D g2 = DrawUtil.init(g);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), DrawUtil.RoundingDiameter, DrawUtil.RoundingDiameter);
    }

    private final class MessageLabel extends SamebugMultilineLabel {
        {
            setText(TipHit.this.model.message);
            setForeground(ColorUtil.TipText);
        }
    }

    private final class AuthorPanel extends TransparentPanel {
        private final static int AvatarIconSize = 26;
        private final SamebugLabel name;
        private final SamebugLabel timestamp;

        {
            final AvatarIcon authorIcon = new AvatarIcon(model.createdByAvatarUrl, AvatarIconSize);
            name = new SamebugLabel(model.createdBy, FontRegistry.regular(12));
            name.setForeground(ColorUtil.UnemphasizedText);
            timestamp = new SamebugLabel(TextUtil.prettyTime(model.createdAt), FontRegistry.regular(12));
            timestamp.setForeground(ColorUtil.UnemphasizedText);

            setLayout(new MigLayout("", "0[]5[]0", "0[14!]0[14!]0"));

            add(authorIcon, "cell 0 0, spany 2");
            add(name, "cell 1 0");
            add(timestamp, "cell 1 1");
        }
    }

    @Nullable
    @Override
    public Object getData(@NonNls String dataId) {
        if (MarkButton.SolutionId.is(dataId)) return model.solutionId;
        else return null;
    }
}
