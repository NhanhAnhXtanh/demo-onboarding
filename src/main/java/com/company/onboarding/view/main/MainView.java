package com.company.onboarding.view.main;

import com.company.onboarding.app.SessionBoot;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("")
@ViewController(id = "MainView")
@ViewDescriptor(path = "main-view.xml")
public class MainView extends StandardMainView {

    private static final Logger log = LoggerFactory.getLogger(MainView.class);
    private final SessionBoot sessionBoot;

    public MainView(SessionBoot sessionBoot) {
        this.sessionBoot = sessionBoot;
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent e) {
        sessionBoot.refreshDepts(); // đảm bảo departmentId đã nằm trong SessionData
    }


}
