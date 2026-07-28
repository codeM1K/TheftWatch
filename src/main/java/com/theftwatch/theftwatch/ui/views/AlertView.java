package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.service.AlertService;
import com.theftwatch.theftwatch.domain.Alert;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "alerts", layout = com.theftwatch.theftwatch.ui.MainLayout.class)
@PageTitle("Alerts")
public class AlertView extends VerticalLayout {

    private final AlertService alertService;
    private final Grid<Alert> alertGrid = new Grid<>(Alert.class);

    public AlertView(AlertService alertService) {
        this.alertService = alertService;
        alertGrid.setColumns("title", "severity", "status", "detectedAt");
        alertGrid.setSizeFull();

        alertGrid.addSelectionListener(event -> {
            Alert selected = event.getFirstSelectedItem().orElse(null);
            if (selected != null) {
                showAlertDetail(selected);
            }
        });

        add(alertGrid);
        refreshGrid();
    }

    private void showAlertDetail(Alert alert) {
        Div detail = new Div();
        detail.addClassName("alert-detail");

        StringBuilder sb = new StringBuilder();
        sb.append("<h3>").append(alert.getTitle()).append("</h3>");
        sb.append("<p><b>Severity:</b> ").append(alert.getSeverity()).append("</p>");
        sb.append("<p><b>Status:</b> ").append(alert.getStatus()).append("</p>");
        sb.append("<p><b>Detected:</b> ").append(alert.getDetectedAt()).append("</p>");
        if (alert.getDescription() != null) {
            sb.append("<p><b>Description:</b> ").append(alert.getDescription()).append("</p>");
        }

        detail.getElement().setProperty("innerHTML", sb.toString());
        add(detail);
    }

    private void refreshGrid() {
        alertGrid.setItems(alertService.findAll());
    }
}
