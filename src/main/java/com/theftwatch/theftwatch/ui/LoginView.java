package com.theftwatch.theftwatch.ui;

import com.theftwatch.theftwatch.security.SecurityService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "login")
@PageTitle("TheftWatch - Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final AuthenticationManager authenticationManager;
    private final SecurityService securityService;

    public LoginView(AuthenticationManager authenticationManager, SecurityService securityService) {
        this.authenticationManager = authenticationManager;
        this.securityService = securityService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("TheftWatch VMS");
        title.getStyle().set("margin-bottom", "20px");

        TextField email = new TextField("Email");
        email.setWidth("300px");

        PasswordField password = new PasswordField("Password");
        password.setWidth("300px");

        com.vaadin.flow.component.button.Button loginButton = new com.vaadin.flow.component.button.Button("Login", event -> {
            try {
                var authToken = new UsernamePasswordAuthenticationToken(email.getValue(), password.getValue());
                var auth = authenticationManager.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                UI.getCurrent().getPage().setLocation("/");
            } catch (Exception e) {
                Notification.show("Invalid credentials", 3000, Notification.Position.MIDDLE);
            }
        });

        add(new Html("<div style='text-align:center'></div>"), title, email, password, loginButton);
    }
}
