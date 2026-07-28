package com.theftwatch.theftwatch.ui;

import com.theftwatch.theftwatch.domain.enums.Role;
import com.theftwatch.theftwatch.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;

        VerticalLayout drawer = new VerticalLayout();
        drawer.setPadding(true);
        drawer.setSpacing(false);
        drawer.setWidthFull();

        if (securityService.isLoggedIn()) {
            drawer.add(createNavLink("Dashboard", ""));
            drawer.add(createNavLink("Cameras", "cameras"));
            drawer.add(createNavLink("Alerts", "alerts"));

            if (securityService.hasRole(Role.SUPER_ADMIN) || securityService.hasRole(Role.SPECIAL_ADMIN)) {
                drawer.add(createNavLink("Realms", "realms"));
                drawer.add(createNavLink("Users", "users"));
            }

            if (securityService.hasRole(Role.SUPER_ADMIN)) {
                drawer.add(createNavLink("Administration", "admin"));
            }

            drawer.add(createNavLink("Logout", "/logout"));
        }

        HorizontalLayout header = new HorizontalLayout(new H1("TheftWatch"));
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
        addToDrawer(drawer);
    }

    private Anchor createNavLink(String text, String route) {
        return new Anchor(route, text);
    }
}
