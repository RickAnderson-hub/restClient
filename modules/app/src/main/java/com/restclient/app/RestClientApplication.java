package com.restclient.app;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Process entry point. Delegates straight to JavaFX's {@link Application#launch};
 * the Spring context itself is started inside {@link JavaFxApplication#init()}
 * so that the UI thread and the Spring lifecycle are correctly sequenced.
 *
 * <p>The {@code @SpringBootApplication} scan base is the {@code com.restclient}
 * root, so beans in the service, persistence, and ui modules are all picked up.
 */
@SpringBootApplication(scanBasePackages = "com.restclient")
public class RestClientApplication {

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }
}
