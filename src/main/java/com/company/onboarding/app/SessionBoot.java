package com.company.onboarding.app;

import com.company.onboarding.entity.Department;
import io.jmix.core.DataManager;
import io.jmix.core.session.SessionData;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SessionBoot {

    private static final Logger log = LoggerFactory.getLogger(SessionBoot.class);
    private final ObjectProvider<SessionData> sdProvider;
    private final CurrentUserSubstitution cus;
    private final DataManager dataManager;

    public SessionBoot(ObjectProvider<SessionData> sdProvider,
                       CurrentUserSubstitution cus,
                       DataManager dataManager) {
        this.sdProvider = sdProvider;
        this.cus = cus;
        this.dataManager = dataManager;
    }

    /** Lưu tất cả department mà effective user làm hrManager */
    public void refreshDepts() {
        var sd = sdProvider.getObject();
        var eff = (com.company.onboarding.entity.User) cus.getEffectiveUser();

        List<UUID> ids = dataManager.load(Department.class)
                .query("select d from Department d where d.hrManager.id = :uid")
                .parameter("uid", eff.getId())
                .list()
                .stream()
                .map(Department::getId)
                .toList();

        sd.setAttribute("departmentIds", ids);
        log.info("departmentIds in session: {}", ids);

    }
}
