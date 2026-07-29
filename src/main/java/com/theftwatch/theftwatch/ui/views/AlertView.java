package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.service.AlertService;
import com.theftwatch.theftwatch.domain.Alert;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

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

        Button refreshButton = new Button("Refresh", event -> refreshGrid());
        add(refreshButton, alertGrid);
        setSizeFull();
        refreshGrid();
    }

    private void showAlertDetail(Alert alert) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setHeaderTitle("Alert Details");

        VerticalLayout content = new VerticalLayout();
        content.add(new H3(alert.getTitle()));
        content.add(new Paragraph("Severity: " + alert.getSeverity()));
        content.add(new Paragraph("Status: " + alert.getStatus()));
        content.add(new Paragraph("Detected: " + alert.getDetectedAt()));
        if (alert.getDescription() != null && !alert.getDescription().isEmpty()) {
            content.add(new Paragraph("Description: " + alert.getDescription()));
        }

        dialog.add(content);
        Button closeButton = new Button("Close", event -> dialog.close());
        dialog.getFooter().add(closeButton);
        dialog.open();
    }

    private void refreshGrid() {
        List<Alert> alerts = alertService.findAll();
        alertGrid.setItems(alerts);
    }
}
