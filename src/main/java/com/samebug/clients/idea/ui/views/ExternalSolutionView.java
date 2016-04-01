package com.samebug.clients.idea.ui.views;

import com.samebug.clients.idea.resources.SamebugIcons;
import com.samebug.clients.idea.ui.ColorUtil;
import com.samebug.clients.idea.ui.Colors;
import com.samebug.clients.idea.ui.ImageUtil;
import com.samebug.clients.idea.ui.components.*;
import com.samebug.clients.search.api.entities.legacy.BreadCrumb;
import com.samebug.clients.search.api.entities.legacy.RestHit;
import com.samebug.clients.search.api.entities.legacy.SolutionReference;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;

/**
 * Created by poroszd on 3/29/16.
 */
public class ExternalSolutionView {
    final RestHit<SolutionReference> solution;
    final java.util.List<BreadCrumb> searchBreadcrumb;
    final String packageName = "???";
    final String className = "???";

    public JPanel controlPanel;
    public JPanel titlePanel;
    public JLabel messageLabel;
    public JLabel titleLabel;
    public SourceReferencePanel sourceReferencePanel;
    public JPanel actionPanel;
    public JPanel breadcrumbPanel;

    public ExternalSolutionView(RestHit<SolutionReference> solution, java.util.List<BreadCrumb> searchBreadcrumb) {
        this.solution = solution;
        this.searchBreadcrumb = searchBreadcrumb;

//        int dotIndex = solution.exception.typeName.lastIndexOf('.');
//        if (dotIndex < 0) {
//            this.packageName = null;
//            this.className = solution.exception.typeName;
//        } else {
//            this.packageName = solution.exception.typeName.substring(0, dotIndex);
//            this.className = solution.exception.typeName.substring(dotIndex + 1);
//        }

        controlPanel = new ControlPanel();
        breadcrumbPanel = new LegacyBreadcrumbBar(searchBreadcrumb.subList(0, solution.matchLevel));
        titlePanel = new TitlePanel();
        titleLabel = new TitleLabel();
        messageLabel = new ExceptionMessageLabel(null);
        sourceReferencePanel = new SourceReferencePanel(solution.solution);
        actionPanel = new ActionPanel();

        controlPanel.add(new JPanel() {
            {
                setLayout(new BorderLayout(0, 0));
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
                setOpaque(false);
                add(breadcrumbPanel, BorderLayout.SOUTH);
                add(titlePanel, BorderLayout.NORTH);
                add(new JPanel() {
                    {
                        setLayout(new BorderLayout(0, 0));
                        setBorder(BorderFactory.createEmptyBorder());
                        setOpaque(false);
                        add(actionPanel, BorderLayout.SOUTH);
                        add(new JPanel() {
                            {
                                setLayout(new BorderLayout(0, 0));
                                setBorder(BorderFactory.createEmptyBorder());
                                setOpaque(false);
                                add(sourceReferencePanel, BorderLayout.SOUTH);
                                add(titleLabel, BorderLayout.NORTH);
                                add(messageLabel, BorderLayout.CENTER);

                            }
                        }, BorderLayout.CENTER);
                    }
                }, BorderLayout.CENTER);
            }
        }, BorderLayout.CENTER);
    }



    public class ControlPanel extends JPanel {
        {
            setLayout(new BorderLayout(0, 0));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        }
    }

    public class TitlePanel extends JPanel {
        {
            setLayout(new BorderLayout(0, 0));
            setBorder(BorderFactory.createEmptyBorder());
            setOpaque(false);
            // TODO load image asynchronously
            // TODO do not use getScaledInstance; https://community.oracle.com/docs/DOC-983611
            final Image sourceIcon = ImageUtil.getImage(solution.solution.source.iconUrl).getScaledInstance(32, 32, Image.SCALE_FAST);
            add(new JPanel() {
                {
                    setOpaque(false);
                    setBorder(BorderFactory.createEmptyBorder());
                    add(new JLabel(new ImageIcon(sourceIcon)));
                }
            }, BorderLayout.WEST);
            add(new LinkLabel(solution.solution.title, solution.solution.url) {
                {
                    HashMap<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
                    attributes.put(TextAttribute.SIZE, 16);
                    setFont(getFont().deriveFont(attributes));
                    setForeground(Colors.samebugOrange);
                }
            }, BorderLayout.CENTER);
        }
    }

    public class TitleLabel extends JLabel {
        @Override
        public String getText() {
            return String.format("%s", className);
        }
    }



    public class ActionPanel extends JPanel {
        {
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setOpaque(false);
//            add(new MarkPanel(solution));
        }
    }
}
