package com.restclient.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

/**
 * Bridges the JavaFX and Spring lifecycles.
 *
 * <ul>
 *   <li>{@link #init()} runs before the UI thread starts the stage — we boot the
 *       Spring context here so all beans are ready when controllers load.</li>
 *   <li>{@link #start(Stage)} loads the root FXML, wiring the
 *       {@link FXMLLoader} to Spring's bean factory so {@code @Component}
 *       controllers are dependency-injected rather than new'd by JavaFX.</li>
 *   <li>{@link #stop()} closes the context and signals the JVM to exit.</li>
 * </ul>
 */
public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .build(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(
                        getClass().getResource("/fxml/main-window.fxml"),
                        "main-window.fxml not found on classpath"));

        // Hand control instantiation to Spring so controllers get their beans injected.
        loader.setControllerFactory(applicationContext::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("REST Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    /**
     * Small helper to keep the {@link SpringApplication} setup readable and to
     * mark the app as non-web (no embedded Tomcat for a desktop UI).
     */
    private static final class SpringApplicationBuilder {
        ConfigurableApplicationContext build(String[] args) {
            SpringApplication application = new SpringApplication(RestClientApplication.class);
            application.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
            application.setHeadless(false); // JavaFX needs a real display
            return application.run(args);
        }
    }
}
