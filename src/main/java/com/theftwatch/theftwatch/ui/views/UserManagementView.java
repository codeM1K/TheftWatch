package com.theftwatch.theftwatch.ui.views;

import com.theftwatch.theftwatch.domain.User;
import com.theftwatch.theftwatch.domain.enums.Role;
import com.theftwatch.theftwatch.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

        FormLayout form = new FormLayout(emailField, fullNameField, passwordField, roleComboBox);
        Button addButton = new Button("Add User", event -> addUser());
        HorizontalLayout buttons = new HorizontalLayout(addButton);

        add(form, buttons, userGrid);
        setSizeFull();
        refreshGrid();
    }

    private void addUser() {
        User user = userService.createUser(
                emailField.getValue(),
                passwordField.getValue(),
                fullNameField.getValue(),
                roleComboBox.getValue(),
                null
        );
        Notification.show("User created: " + user.getEmail());
        refreshGrid();
        clearForm();
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
