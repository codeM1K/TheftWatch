package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.domain.User;
import com.theftwatch.theftwatch.domain.enums.Role;
import com.theftwatch.theftwatch.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "users", layout = com.theftwatch.theftwatch.ui.MainLayout.class)
@PageTitle("User Management")
public class UserManagementView extends VerticalLayout {

    private final UserService userService;

    private final Grid<User> userGrid = new Grid<>(User.class);
    private final TextField emailField = new TextField("Email");
    private final TextField fullNameField = new TextField("Full Name");
    private final PasswordField passwordField = new PasswordField("Password");
    private final ComboBox<Role> roleComboBox = new ComboBox<>("Role");

    public UserManagementView(UserService userService) {
        this.userService = userService;

        userGrid.setColumns("email", "fullName", "role", "enabled", "createdAt");
        userGrid.setSizeFull();

        roleComboBox.setItems(Role.values());
        roleComboBox.setItemLabelGenerator(role -> role.name());

        userGrid.addSelectionListener(event -> {
            User selected = event.getFirstSelectedItem().orElse(null);
            if (selected != null) {
                openEditDialog(selected);
            }
        });

        FormLayout form = new FormLayout(emailField, fullNameField, passwordField, roleComboBox);
        Button addButton = new Button("Add User", event -> addUser());
        Button deleteButton = new Button("Delete Selected", event -> deleteSelectedUser());
        HorizontalLayout buttons = new HorizontalLayout(addButton, deleteButton);

        add(form, buttons, userGrid);
        setSizeFull();
        refreshGrid();
    }

    private void addUser() {
        if (emailField.getValue() == null || emailField.getValue().isEmpty()) {
            Notification.show("Email is required", 3000, Notification.Position.MIDDLE);
            return;
        }
        if (passwordField.getValue() == null || passwordField.getValue().isEmpty()) {
            Notification.show("Password is required", 3000, Notification.Position.MIDDLE);
            return;
        }
        User user = userService.createUser(
                emailField.getValue(),
                passwordField.getValue(),
                fullNameField.getValue(),
                roleComboBox.getValue() != null ? roleComboBox.getValue() : Role.END_USER,
                null
        );
        Notification.show("User created: " + user.getEmail());
        refreshGrid();
        clearForm();
    }

    private void deleteSelectedUser() {
        User selected = userGrid.asSingleSelect().getValue();
        if (selected != null) {
            Notification.show("User deleted: " + selected.getEmail());
            refreshGrid();
        }
    }

    private void openEditDialog(User user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeaderTitle("Edit User");

        TextField emailField = new TextField("Email");
        emailField.setValue(user.getEmail() != null ? user.getEmail() : "");
        emailField.setEnabled(false);

        TextField fullNameField = new TextField("Full Name");
        fullNameField.setValue(user.getFullName() != null ? user.getFullName() : "");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Leave blank to keep current");

        ComboBox<Role> roleComboBox = new ComboBox<>("Role");
        roleComboBox.setItems(Role.values());
        roleComboBox.setItemLabelGenerator(role -> role.name());
        roleComboBox.setValue(user.getRole());

        FormLayout form = new FormLayout(emailField, fullNameField, passwordField, roleComboBox);
        dialog.add(form);

        Button saveButton = new Button("Save", event -> {
            user.setFullName(fullNameField.getValue());
            user.setRole(roleComboBox.getValue());
            Notification.show("User updated: " + user.getEmail());
            dialog.close();
            refreshGrid();
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        dialog.getFooter().add(buttons);
        dialog.open();
    }

    private void refreshGrid() {
        userGrid.setItems(userService.findAll());
    }

    private void clearForm() {
        emailField.clear();
        fullNameField.clear();
        passwordField.clear();
        roleComboBox.clear();
    }
}
