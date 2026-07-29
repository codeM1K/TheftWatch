package com.theftwatch.theftwatch.ui;

import com.theftwatch.theftwatch.domain.enums.Role;
import com.theftwatch.theftwatch.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;

public class MainLayout extends AppLayout implements RouterLayout {

    private final SecurityService securityService;
    private VerticalLayout drawer;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;

        drawer = new VerticalLayout();
        drawer.setPadding(true);
        drawer.setSpacing(true);
        drawer.setWidthFull();
        drawer.addClassName("main-drawer");

        HorizontalLayout header = new HorizontalLayout(new H1("TheftWatch"));
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.addClassName("main-header");

        addToNavbar(header);
        addToDrawer(drawer);
    }

    public void beforeNavigation(BeforeEvent event) {
        Location location = event.getLocation();
        String path = location.getPath();

        drawer.removeAll();

        if (isAuthenticated()) {
            drawer.add(createNavLink("Dashboard", ""));
            drawer.add(createNavLink("Cameras", "cameras"));
            drawer.add(createNavLink("Alerts", "alerts"));

            if (hasRole(Role.SUPER_ADMIN) || hasRole(Role.SPECIAL_ADMIN)) {
                drawer.add(createNavLink("Realms", "realms"));
                drawer.add(createNavLink("Users", "users"));
            }

            if (hasRole(Role.SUPER_ADMIN)) {
                drawer.add(createNavLink("Administration", "admin"));
            }

            drawer.add(createNavLink("Logout", "/logout"));
        }
    }

    private boolean isAuthenticated() {
        var principal = VaadinServletRequest.getCurrent().getUserPrincipal();
        return principal != null && !"anonymousUser".equals(principal.getName());
    }

    private boolean hasRole(Role role) {
        if (!isAuthenticated()) {
            return false;
        }
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.name()));
    }

    private Anchor createNavLink(String text, String route) {
        return new Anchor(route, text);
    }
}
