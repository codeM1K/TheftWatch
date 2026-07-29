package com.theftwatch.theftwatch.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "admin", layout = com.theftwatch.theftwatch.ui.MainLayout.class)
@PageTitle("Administration")
@AnonymousAllowed
public class AdministrationView extends VerticalLayout {

    public AdministrationView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Administration");
        title.getStyle().set("margin", "0");

        H2 subtitle = new H2("System Administration");
        subtitle.getStyle().set("margin", "0");
        subtitle.getStyle().set("font-weight", "normal");
        subtitle.getStyle().set("color", "#666");

        add(title, subtitle);
    }
}
