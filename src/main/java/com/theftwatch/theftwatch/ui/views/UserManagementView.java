package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.domain.User;
import com.theftwatch.theftwatch.domain.enums.Role;
import com.theftwatch.theftwatch.security.SecurityService;
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
    private final SecurityService securityService;

    private final Grid<User> userGrid = new Grid<>(User.class);
    private final TextField emailField = new TextField("Email");
    private final TextField fullNameField = new TextField("Full Name");
    private final PasswordField passwordField = new PasswordField("Password");
    private final ComboBox<Role> roleComboBox = new ComboBox<>("Role");

    public UserManagementView(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;

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
        String email = emailField.getValue();
        String password = passwordField.getValue();

        if (email == null || email.isBlank()) {
            Notification.show("Email is required", 3000, Notification.Position.MIDDLE);
            return;
        }
        if (password == null || password.isBlank()) {
            Notification.show("Password is required", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            User currentUser = securityService.getCurrentUser();
            User user = userService.createUser(
                    email.trim(),
                    password,
                    fullNameField.getValue(),
                    roleComboBox.getValue() != null ? roleComboBox.getValue() : Role.END_USER,
                    currentUser
            );
            Notification.show("User created: " + user.getEmail(), 3000, Notification.Position.MIDDLE);
            refreshGrid();
            clearForm();
        } catch (Exception e) {
            Notification.show("Failed to create user: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void deleteSelectedUser() {
        User selected = userGrid.asSingleSelect().getValue();
        if (selected != null) {
            try {
                userService.deleteUser(selected.getEmail());
                Notification.show("User deleted: " + selected.getEmail(), 3000, Notification.Position.MIDDLE);
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Failed to delete user: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
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
            try {
                userService.updateUser(
                        user.getEmail(),
                        fullNameField.getValue(),
                        roleComboBox.getValue(),
                        passwordField.getValue()
                );
                Notification.show("User updated: " + user.getEmail(), 3000, Notification.Position.MIDDLE);
                dialog.close();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Failed to update user: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
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
