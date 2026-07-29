package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.domain.Camera;
import com.theftwatch.theftwatch.service.CameraService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEvent;

import java.util.List;

@Route(value = "streaming", layout = com.theftwatch.theftwatch.ui.MainLayout.class)
@PageTitle("Streaming View")
public class StreamingView extends VerticalLayout {

    private final CameraService cameraService;

    private final ComboBox<Camera> cameraComboBox = new ComboBox<>("Select Camera");
    private final Button startStreamButton = new Button("Start Stream");
    private final Button stopStreamButton = new Button("Stop Stream");
    private final Button refreshCamerasButton = new Button("Refresh Cameras");

    public StreamingView(CameraService cameraService) {
        this.cameraService = cameraService;

        cameraComboBox.setItemLabelGenerator(Camera::getName);
        cameraComboBox.setPlaceholder("Select a camera to stream");

        startStreamButton.addClickListener(event -> startSelectedCamera());
        stopStreamButton.addClickListener(event -> stopSelectedCamera());
        refreshCamerasButton.addClickListener(event -> refreshCameraList());

        HorizontalLayout controls = new HorizontalLayout(cameraComboBox, startStreamButton, stopStreamButton, refreshCamerasButton);
        controls.setSizeFull();

        add(controls);
        setSizeFull();
        refreshCameraList();
    }

    public void beforeNavigation(BeforeEvent event) {
        refreshCameraList();
    }

    private void refreshCameraList() {
        List<Camera> cameras = cameraService.findAll();
        cameraComboBox.setItems(cameras);
        if (cameras.isEmpty()) {
            cameraComboBox.setPlaceholder("No cameras available");
            Notification.show("No cameras found. Please add cameras first.", 3000, Notification.Position.MIDDLE);
        } else {
            cameraComboBox.setPlaceholder("Select a camera");
        }
    }

    private void startSelectedCamera() {
        Camera selected = cameraComboBox.getValue();
        if (selected != null) {
            try {
                cameraService.startStream(selected.getId());
                Notification.show("Stream started for: " + selected.getName(), 3000, Notification.Position.MIDDLE);
                // In a real implementation this would update the stream display
            } catch (Exception e) {
                Notification.show("Failed to start stream: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        } else {
            Notification.show("Please select a camera first", 3000, Notification.Position.MIDDLE);
        }
    }

    private void stopSelectedCamera() {
        Camera selected = cameraComboBox.getValue();
        if (selected != null) {
            try {
                cameraService.stopStream(selected.getId());
                Notification.show("Stream stopped for: " + selected.getName(), 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Failed to stop stream: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        } else {
            Notification.show("Please select a camera first", 3000, Notification.Position.MIDDLE);
        }
    }
}