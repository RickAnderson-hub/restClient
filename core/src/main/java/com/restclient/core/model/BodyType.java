package com.restclient.core.model;

/**
 * The kind of payload a request body carries. Drives both the editor mode in
 * the UI and the {@code Content-Type} applied when the request is sent.
 */
public enum BodyType {
    NONE(null),
    JSON("application/json"),
    XML("application/xml"),
    RAW_TEXT("text/plain"),
    FORM_URLENCODED("application/x-www-form-urlencoded"),
    FORM_DATA("multipart/form-data"),
    BINARY("application/octet-stream");

    private final String defaultContentType;

    BodyType(String defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    /**
     * The Content-Type header value implied by this body type, or {@code null}
     * for {@link #NONE}.
     */
    public String getDefaultContentType() {
        return defaultContentType;
    }
}
