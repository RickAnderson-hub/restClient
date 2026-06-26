package com.restclient.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The result of executing a {@link Request}. Captures status, headers, body,
 * and timing/size metadata used by the response viewer.
 */
@Getter
@Setter
@ToString(of = {"statusCode", "statusText", "durationMillis", "sizeBytes"})
public final class Response {

    private String id;
    private String requestId;
    private int statusCode;
    private String statusText;
    private Map<String, String> headers;
    private String body;
    private long durationMillis;
    private long sizeBytes;
    private String contentType;
    private LocalDateTime timestamp;

    public Response() {
        this.id = UUID.randomUUID().toString();
        this.headers = new LinkedHashMap<>();
        this.timestamp = LocalDateTime.now();
    }

    /** Returns {@code true} for 2xx status codes. */
    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }
}
