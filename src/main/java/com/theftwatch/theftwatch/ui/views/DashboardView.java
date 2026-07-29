package com.theftwatch.theftwatch.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = com.theftwatch.theftwatch.ui.MainLayout.class)
@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("TheftWatch VMS");
        title.getStyle().set("margin-bottom", "20px");

        H2 subtitle = new H2("Dashboard");
        subtitle.getStyle().set("margin-bottom", "20px");
        subtitle.getStyle().set("font-weight", "normal");
        subtitle.getStyle().set("color", "#666");

        add(title, subtitle);
    }
}
