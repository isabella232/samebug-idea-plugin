package com.samebug.clients.swing.ui.frame;

import com.samebug.clients.common.ui.component.community.IHelpOthersCTA;
import com.samebug.clients.common.ui.component.hit.IMarkButton;
import com.samebug.clients.common.ui.component.hit.ITipHit;
import com.samebug.clients.common.ui.frame.helpRequest.IHelpRequestTabs;
import com.samebug.clients.swing.ui.TestDialog;
import com.samebug.clients.swing.ui.frame.helpRequest.HelpRequestFrame;
import com.samebug.clients.swing.ui.modules.ComponentService;
import com.samebug.clients.swing.ui.modules.ListenerService;
import org.junit.Test;

import java.awt.*;
import java.util.Date;

public class HelpRequestFrameTest extends TestDialog {
    @Test
    public void zeroTipsAndZeroWebHits() {
        waitToInitializeUI("helpRequest/t0w0.json").showDialog();
    }

    @Test
    public void twoTipsAndTwoWebHits() {
        waitToInitializeUI("helpRequest/t2w2.json").showDialog();
    }

    protected void initializeUI(String resourceJson) throws Exception {
        final HelpRequestFrame.Model model = readJson(resourceJson, HelpRequestFrame.Model.class);

        HelpRequestFrame f = new HelpRequestFrame();
        f.loadingSucceeded(model);
        setContentPane(f);

        ListenerService.putListenerToComponent(f, IHelpOthersCTA.Listener.class, new IHelpOthersCTA.Listener() {
            @Override
            public void postTip(IHelpOthersCTA source, String tipBody) {
                ITipHit.Model newTip = new ITipHit.Model(tipBody, 1, 0, "doc", new Date(), "me", null,
                        new IMarkButton.Model(0, null, false));
                IHelpRequestTabs tabs = ComponentService.findAncestor((Component) source, IHelpRequestTabs.class);
                assert tabs != null;

                source.successPostTip(newTip);
                tabs.sentResponse(newTip);
            }
        });
    }
}
