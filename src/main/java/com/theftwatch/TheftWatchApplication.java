package com.theftwatch;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@Theme(value = "lumo")
public class TheftWatchApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TheftWatchApplication.class, args);
    }
}
