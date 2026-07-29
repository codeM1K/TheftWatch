package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.domain.Realm;
import com.theftwatch.theftwatch.service.RealmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEvent;

import java.util.List;

@Route(value = "realms", layout = com.theftwatch.theftwatch.ui.MainLayout.class)
@PageTitle("Realm Management")
public class RealmManagementView extends VerticalLayout {

    private final RealmService realmService;

    private final Grid<Realm> realmGrid = new Grid<>(Realm.class);
    private final TextField nameField = new TextField("Realm Name");
    private final TextArea descriptionField = new TextArea("Description");

    public RealmManagementView(RealmService realmService) {
        this.realmService = realmService;

        realmGrid.setColumns("name", "description", "createdAt");
        realmGrid.setSizeFull();

        realmGrid.addSelectionListener(event -> {
            Realm selected = event.getFirstSelectedItem().orElse(null);
            if (selected != null) {
                openEditDialog(selected);
            }
        });

        FormLayout form = new FormLayout(nameField, descriptionField);
        Button addButton = new Button("Add Realm", event -> addRealm());
        Button refreshButton = new Button("Refresh", event -> refreshGrid());
        HorizontalLayout buttons = new HorizontalLayout(addButton, refreshButton);

        add(form, buttons, realmGrid);
        setSizeFull();
        refreshGrid();
    }

    public void beforeNavigation(BeforeEvent event) {
        refreshGrid();
    }

    private void addRealm() {
        String name = nameField.getValue();
        String description = descriptionField.getValue();

        if (name == null || name.isBlank()) {
            Notification.show("Realm name is required", 3000, Notification.Position.MIDDLE);
            nameField.focus();
            return;
        }

        try {
            Realm realm = realmService.createRealm(name.trim(), description != null ? description : "", null);
            Notification.show("Realm created: " + realm.getName(), 3000, Notification.Position.MIDDLE);
            clearForm();
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Failed to create realm: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void openEditDialog(Realm realm) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeaderTitle("Edit Realm");

        TextField nameField = new TextField("Realm Name");
        nameField.setValue(realm.getName() != null ? realm.getName() : "");

        TextArea descriptionField = new TextArea("Description");
        descriptionField.setValue(realm.getDescription() != null ? realm.getDescription() : "");

        FormLayout form = new FormLayout(nameField, descriptionField);
        dialog.add(form);

        Button saveButton = new Button("Save", event -> {
            try {
                realm.setName(nameField.getValue());
                realm.setDescription(descriptionField.getValue());
                Notification.show("Realm updated: " + realm.getName(), 3000, Notification.Position.MIDDLE);
                dialog.close();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Failed to update realm: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        dialog.getFooter().add(buttons);
        dialog.open();
    }

    private void refreshGrid() {
        realmGrid.setItems(realmService.findAll());
    }

    private void clearForm() {
        nameField.clear();
        descriptionField.clear();
    }
}
