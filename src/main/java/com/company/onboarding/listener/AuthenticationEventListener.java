package com.company.onboarding.listener;

import com.company.onboarding.entity.User;
import io.jmix.core.session.SessionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;



@Component
public class AuthenticationEventListener {

    private static final Logger log =
            LoggerFactory.getLogger(AuthenticationEventListener.class);

    @Autowired
    private ObjectFactory<SessionData> sessionDataFactory;

    @EventListener
    public void onAuthenticationSuccess(final AuthenticationSuccessEvent event) {
        User user = (User) event.getAuthentication().getPrincipal();
        log.info("User authenticated " + user.getUsername());
    }

    @EventListener
    public void onInteractiveAuthenticationSuccess(final InteractiveAuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            if (user.getDepartment() != null) {
                SessionData sessionData = sessionDataFactory.getObject();
                sessionData.setAttribute("departmentId", user.getDepartment().getId());
                log.info("departmentId saved to session: {}", user.getDepartment().getId());
            }
        }

    }

    @EventListener
    public void onAuthenticationFailure(final AbstractAuthenticationFailureEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        log.info("User login attempt failed: " + username);
    }

    @EventListener
    public void onLogoutSuccess(final LogoutSuccessEvent event) {
        User user = (User) event.getAuthentication().getPrincipal();
        log.info("User logged out: " + user.getUsername());
    }
}