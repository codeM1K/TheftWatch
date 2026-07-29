package com.theftwatch.theftwatch.ui;

import com.theftwatch.theftwatch.service.AlertService;
import com.theftwatch.theftwatch.service.CameraService;
import com.theftwatch.theftwatch.service.RealmService;
import com.theftwatch.theftwatch.service.UserService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "", layout = MainLayout.class)
@PageTitle("TheftWatch - Dashboard")
@AnonymousAllowed
public class DashboardView extends VerticalLayout {

    private final CameraService cameraService;
    private final AlertService alertService;
    private final RealmService realmService;
    private final UserService userService;

    public DashboardView(CameraService cameraService, AlertService alertService,
                         RealmService realmService, UserService userService) {
        this.cameraService = cameraService;
        this.alertService = alertService;
        this.realmService = realmService;
        this.userService = userService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Dashboard");
        title.getStyle().set("margin", "0");

        add(title);

        HorizontalLayout stats = createStatsCards();
        add(stats);

        H2 recentAlertsTitle = new H2("Recent Alerts");
        recentAlertsTitle.getStyle().set("margin-top", "20px");
        add(recentAlertsTitle);

        VerticalLayout alertsContainer = createRecentAlerts();
        add(alertsContainer);
    }

    private HorizontalLayout createStatsCards() {
        long totalCameras = cameraService.findAll().size();
        long totalAlerts = alertService.findAll().size();
        long totalRealms = realmService.findAll().size();
        long totalUsers = userService.findAll().size();

        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setSpacing(true);
        cards.setPadding(false);

        cards.add(createStatCard("Cameras", String.valueOf(totalCameras)));
        cards.add(createStatCard("Alerts", String.valueOf(totalAlerts)));
        cards.add(createStatCard("Realms", String.valueOf(totalRealms)));
        cards.add(createStatCard("Users", String.valueOf(totalUsers)));

        return cards;
    }

    private VerticalLayout createStatCard(String title, String value) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
                .set("background", "#f5f5f5")
                .set("border-radius", "8px")
                .set("padding", "20px")
                .set("min-width", "150px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        Span titleSpan = new Span(title);
        titleSpan.getStyle().set("font-size", "14px").set("color", "#666");

        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("font-size", "32px").set("font-weight", "bold").set("color", "#1976d2");

        card.add(titleSpan, valueSpan);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return card;
    }

    private VerticalLayout createRecentAlerts() {
        VerticalLayout container = new VerticalLayout();
        container.setWidthFull();
        container.setPadding(false);
        container.setSpacing(true);

        var alerts = alertService.findAll().stream()
                .sorted((a, b) -> b.getDetectedAt().compareTo(a.getDetectedAt()))
                .limit(5)
                .toList();

        if (alerts.isEmpty()) {
            Span empty = new Span("No alerts yet.");
            empty.getStyle().set("color", "#999");
            container.add(empty);
        } else {
            for (var alert : alerts) {
                HorizontalLayout row = new HorizontalLayout();
                row.setWidthFull();
                row.setSpacing(true);
                row.setPadding(true);
                row.getStyle()
                        .set("background", "#fafafa")
                        .set("border-radius", "4px")
                        .set("align-items", "center");

                Span titleSpan = new Span(alert.getTitle());
                titleSpan.getStyle().set("font-weight", "500");

                Span severitySpan = new Span(alert.getSeverity().name());
                severitySpan.getStyle()
                        .set("font-size", "12px")
                        .set("padding", "2px 8px")
                        .set("border-radius", "12px")
                        .set("background", "#e3f2fd")
                        .set("color", "#1976d2");

                Span timeSpan = new Span(alert.getDetectedAt().toString());
                timeSpan.getStyle().set("color", "#666").set("font-size", "13px");

                row.add(titleSpan, severitySpan, timeSpan);
                row.setFlexGrow(1, titleSpan);
                container.add(row);
            }
        }

        return container;
    }
}
