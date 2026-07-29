package com.theftwatch.theftwatch.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainLayout extends AppLayout {

    public MainLayout() {
        VerticalLayout drawer = new VerticalLayout();
        drawer.setPadding(true);
        drawer.setSpacing(true);
        drawer.setWidthFull();
        drawer.addClassName("main-drawer");

        drawer.add(createNavLink("Dashboard", ""));
        drawer.add(createNavLink("Cameras", "cameras"));
        drawer.add(createNavLink("Alerts", "alerts"));
        drawer.add(createNavLink("Realms", "realms"));
        drawer.add(createNavLink("Users", "users"));
        drawer.add(createNavLink("Administration", "admin"));
        drawer.add(createNavLink("Logout", "/logout"));

        Image logo = new Image("images/logo.png", "TheftWatch Logo");
        logo.getStyle().set("height", "40px").set("width", "auto").set("padding", "4px 0");

        HorizontalLayout header = new HorizontalLayout(logo);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.addClassName("main-header");

        addToNavbar(header);
        addToDrawer(drawer);
    }

    private Anchor createNavLink(String text, String route) {
        return new Anchor(route, text);
    }
}
