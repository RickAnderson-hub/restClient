package com.restclient.ui.controller;

import com.restclient.core.model.HttpMethod;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

/**
 * Controller for the application's root window. At this skeleton stage it only
 * wires up the request bar (method + URL + send) and a placeholder response
 * area; business logic is delegated to services in later phases.
 *
 * <p>Annotated {@code @Component} so Spring constructs it and the
 * {@code FXMLLoader} (via the controller factory) receives a managed instance
 * with dependencies injected.
 */
@Component
public class MainWindowController {

    @FXML
    private ComboBox<HttpMethod> methodComboBox;

    @FXML
    private TextField urlField;

    @FXML
    private TextArea responseArea;

    @FXML
    private Label statusLabel;

    /**
     * Called automatically by the {@link javafx.fxml.FXMLLoader} once the FXML
     * fields are injected.
     */
    @FXML
    public void initialize() {
        methodComboBox.setItems(FXCollections.observableArrayList(HttpMethod.values()));
        methodComboBox.getSelectionModel().select(HttpMethod.GET);
        statusLabel.setText("Ready");
    }

    /**
     * Placeholder send handler — wired to the Send button in FXML. Real HTTP
     * execution arrives in Phase 2 via the service layer.
     */
    @FXML
    public void onSend() {
        HttpMethod method = methodComboBox.getValue();
        String url = urlField.getText();
        statusLabel.setText("Would send " + method + " " + (url == null || url.isBlank() ? "(no URL)" : url));
        responseArea.setText("Response viewer placeholder.\n\nHTTP execution is wired up in the next phase.");
    }
}
