package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.domain.Realm;
import com.theftwatch.theftwatch.service.RealmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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

        FormLayout form = new FormLayout(nameField, descriptionField);
        Button addButton = new Button("Add Realm", event -> addRealm());
        HorizontalLayout buttons = new HorizontalLayout(addButton);

        add(form, buttons, realmGrid);
        setSizeFull();
        refreshGrid();
    }

    private void addRealm() {
        Realm realm = realmService.createRealm(nameField.getValue(), descriptionField.getValue(), null);
        Notification.show("Realm created: " + realm.getName());
        refreshGrid();
        clearForm();
    }

    private void refreshGrid() {
        realmGrid.setItems(realmService.findAll());
    }

    private void clearForm() {
        nameField.clear();
        descriptionField.clear();
    }
}
