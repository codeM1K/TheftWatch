package com.theftwatch.theftwatch.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "", layout = MainLayout.class)
@PageTitle("TheftWatch - Dashboard")
@AnonymousAllowed
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        add(new H1("TheftWatch VMS Dashboard"));
        add("Welcome to TheftWatch - Advanced Video Management System");
    }
}
