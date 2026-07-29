package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.domain.Camera;
import com.theftwatch.theftwatch.domain.Realm;
import com.theftwatch.theftwatch.service.CameraService;
import com.theftwatch.theftwatch.service.RealmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEvent;

import java.util.List;

@Route(value = "cameras", layout = com.theftwatch.theftwatch.ui.MainLayout.class)
@PageTitle("Camera Management")
public class CameraManagementView extends VerticalLayout {

    private final CameraService cameraService;
    private final RealmService realmService;

    private final Grid<Camera> cameraGrid = new Grid<>(Camera.class);
    private final TextField nameField = new TextField("Camera Name");
    private final TextField modelField = new TextField("Model");
    private final TextField rtspUrlField = new TextField("RTSP URL");
    private final TextField usernameField = new TextField("Username");
    private final TextField passwordField = new TextField("Password");
    private final TextField ipAddressField = new TextField("IP Address");
    private final ComboBox<Realm> realmComboBox = new ComboBox<>("Realm");

    public CameraManagementView(CameraService cameraService, RealmService realmService) {
        this.cameraService = cameraService;
        this.realmService = realmService;

        cameraGrid.setColumns("name", "model", "ipAddress", "status");
        cameraGrid.setSizeFull();

        realmComboBox.setItemLabelGenerator(Realm::getName);
        realmComboBox.setPlaceholder("Select realm");

        FormLayout form = new FormLayout(nameField, modelField, rtspUrlField,
                                         usernameField, passwordField, ipAddressField, realmComboBox);

        Button addButton = new Button("Add Camera", event -> addCamera());
        Button startStreamButton = new Button("Start Stream", event -> startSelectedCamera());
        Button stopStreamButton = new Button("Stop Stream", event -> stopSelectedCamera());
        Button deleteButton = new Button("Delete", event -> deleteSelectedCamera());
        Button refreshRealmsButton = new Button("Refresh Realms", event -> refreshRealmComboBox());

        HorizontalLayout buttons = new HorizontalLayout(addButton, startStreamButton, stopStreamButton, deleteButton, refreshRealmsButton);

        add(form, buttons, cameraGrid);
        setSizeFull();
        refreshGrid();
        refreshRealmComboBox();
    }

    public void beforeNavigation(com.vaadin.flow.router.BeforeEvent event) {
        refreshRealmComboBox();
    }

    private void addCamera() {
        String name = nameField.getValue();
        Realm realm = realmComboBox.getValue();

        if (name == null || name.isBlank()) {
            Notification.show("Camera name is required", 3000, Notification.Position.MIDDLE);
            return;
        }
        if (realm == null) {
            Notification.show("Please select a realm", 3000, Notification.Position.MIDDLE);
            return;
        }

        Camera camera = cameraService.createCamera(
                name.trim(),
                modelField.getValue(),
                rtspUrlField.getValue(),
                usernameField.getValue(),
                passwordField.getValue(),
                ipAddressField.getValue(),
                realm,
                null
        );

        Notification.show("Camera added: " + camera.getName(), 3000, Notification.Position.MIDDLE);
        refreshGrid();
        clearForm();
    }

    private void deleteSelectedCamera() {
        Camera selected = cameraGrid.asSingleSelect().getValue();
        if (selected != null) {
            cameraService.stopStream(selected.getId());
            Notification.show("Camera deleted: " + selected.getName(), 3000, Notification.Position.MIDDLE);
            refreshGrid();
        }
    }

    private void startSelectedCamera() {
        Camera selected = cameraGrid.asSingleSelect().getValue();
        if (selected != null) {
            cameraService.startStream(selected.getId());
            Notification.show("Stream started for: " + selected.getName(), 3000, Notification.Position.MIDDLE);
            refreshGrid();
        }
    }

    private void stopSelectedCamera() {
        Camera selected = cameraGrid.asSingleSelect().getValue();
        if (selected != null) {
            cameraService.stopStream(selected.getId());
            Notification.show("Stream stopped for: " + selected.getName(), 3000, Notification.Position.MIDDLE);
            refreshGrid();
        }
    }

    private void refreshGrid() {
        cameraGrid.setItems(cameraService.findAll());
    }

    private void refreshRealmComboBox() {
        List<Realm> realms = realmService.findAll();
        realmComboBox.setItems(realms);
        if (realms.isEmpty()) {
            realmComboBox.setPlaceholder("No realms - create one first");
        } else {
            realmComboBox.setPlaceholder("Select realm");
        }
    }

    private void clearForm() {
        nameField.clear();
        modelField.clear();
        rtspUrlField.clear();
        usernameField.clear();
        passwordField.clear();
        ipAddressField.clear();
        realmComboBox.clear();
    }
}
