package com.restclient.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restclient.core.model.BodyType;
import com.restclient.core.model.HttpMethod;
import com.restclient.core.model.Request;
import com.restclient.core.model.RequestBody;
import com.restclient.core.model.Response;
import com.restclient.service.HttpExecutionService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller for the application's root window.
 *
 * <p>HTTP execution runs on a virtual thread so the FX thread is never blocked.
 * All UI mutations after the background call return via {@link Platform#runLater}.
 */
@Component
public class MainWindowController {

    @Autowired
    HttpExecutionService httpExecutionService;

    @FXML
    ComboBox<HttpMethod> methodComboBox;
    @FXML
    TextField urlField;
    @FXML
    TextArea requestBodyArea;
    @FXML
    TextArea responseArea;
    @FXML
    Label statusLabel;
    @FXML
    Button sendButton;

    private final ObjectMapper mapper = new ObjectMapper();

    /** Initialised by {@link javafx.fxml.FXMLLoader} after FXML fields are injected. */
    @FXML
    public void initialize() {
        methodComboBox.setItems(FXCollections.observableArrayList(HttpMethod.values()));
        methodComboBox.getSelectionModel().select(HttpMethod.GET);
        statusLabel.setText("Ready");
    }

    /** Fires when the Send button is clicked. */
    @FXML
    public void onSend() {
        var url = urlField.getText();
        if (url == null || url.isBlank()) {
            statusLabel.setText("Enter a URL");
            return;
        }
        var method = methodComboBox.getValue();
        var bodyText = requestBodyArea.getText();
        var request = Request.builder()
                .method(method)
                .url(url.trim())
                .body(method.supportsBody() && bodyText != null && !bodyText.isBlank()
                        ? new RequestBody(BodyType.JSON, bodyText)
                        : RequestBody.none())
                .build();
        sendButton.setDisable(true);
        responseArea.clear();
        statusLabel.setText("Sending…");
        Thread.ofVirtual().start(() -> {
            try {
                var response = httpExecutionService.execute(request);
                Platform.runLater(() -> showResponse(response));
            } catch (Exception e) {
                Platform.runLater(() -> showError(e.getMessage()));
            } finally {
                Platform.runLater(() -> sendButton.setDisable(false));
            }
        });
    }

    private void showResponse(Response response) {
        statusLabel.setText(response.getStatusCode() + " " + response.getStatusText()
                + "  •  " + response.getDurationMillis() + "ms"
                + "  •  " + formatSize(response.getSizeBytes()));
        var body = response.getBody() != null ? response.getBody() : "";
        responseArea.setText(isJson(response.getContentType()) ? prettyJson(body) : body);
    }

    private void showError(String message) {
        statusLabel.setText("Error");
        responseArea.setText("Request failed:\n\n" + message);
    }

    private boolean isJson(String contentType) {
        return contentType != null && contentType.contains("json");
    }

    private String prettyJson(String raw) {
        try {
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(mapper.readTree(raw));
        } catch (Exception e) {
            return raw;
        }
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }
}
