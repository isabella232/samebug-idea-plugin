package com.samebug.clients.idea.ui.component.solutions;

import com.intellij.util.messages.MessageBus;
import com.samebug.clients.common.ui.TextUtil;
import com.samebug.clients.idea.resources.SamebugBundle;
import com.samebug.clients.idea.ui.ColorUtil;
import com.samebug.clients.idea.ui.FontRegistry;
import com.samebug.clients.idea.ui.component.util.AvatarIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.net.URL;
import java.util.Date;

public final class BugmateHit extends JPanel {
    private final static int AvatarSize = 44;

    private final Model model;
    private final MessageBus messageBus;

    public BugmateHit(MessageBus messageBus, Model model) {
        this.model = new Model(model);
        this.messageBus = messageBus;

        final NameLabel name = new NameLabel();
        final TimestampLabel timestamp = new TimestampLabel();
        final AvatarIcon avatar = new AvatarIcon(model.avatarUrl, AvatarSize);

        setOpaque(false);
        setLayout(new MigLayout("", "0[]10[]0", "0[]0[]0"));

        add(avatar, "cell 0 0, spany 2");
        add(name, "cell 1 0");
        add(timestamp, "cell 1 1");
    }


    private final class NameLabel extends JLabel {
        {
            setText(model.displayName);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setForeground(ColorUtil.text());
            setFont(new Font(FontRegistry.AvenirDemi, Font.PLAIN, 14));
        }
    }

    private final class TimestampLabel extends JLabel {
        {
            setText(SamebugBundle.message("samebug.component.bugmate.hit.occurred", model.nSeen, TextUtil.prettyTime(model.lastSeen)));
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setForeground(ColorUtil.text());
            setFont(new Font(FontRegistry.AvenirRegular, Font.PLAIN, 14));
        }
    }

    public static final class Model {
        private final int userId;
        private final String displayName;
        private final URL avatarUrl;
        private final int nSeen;
        private final Date lastSeen;

        public Model(Model rhs) {
            this(rhs.userId, rhs.displayName, rhs.avatarUrl, rhs.nSeen, rhs.lastSeen);
        }

        public Model(int userId, String displayName, URL avatarUrl, int nSeen, Date lastSeen) {
            this.userId = userId;
            this.displayName = displayName;
            this.avatarUrl = avatarUrl;
            this.nSeen = nSeen;
            this.lastSeen = lastSeen;
        }
    }
}
