package com.restclient.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A saved HTTP request. This is a framework-agnostic domain model — the
 * persistence layer maps it to a JPA entity, and the UI binds to it via a
 * view model. Mutable with a fluent {@link Builder} for ergonomic construction.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name", "method", "url"})
public final class Request {

    private String id;
    private String name;
    private HttpMethod method;
    private String url;
    private List<Header> headers;
    private RequestBody body;
    private String workspaceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Request() {
        this.id = UUID.randomUUID().toString();
        this.method = HttpMethod.GET;
        this.headers = new ArrayList<>();
        this.body = RequestBody.none();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /** Returns a fluent builder starting from the no-arg defaults. */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Fluent builder. Starts from a default {@link Request} (random id, GET,
     * empty headers, no body) and overrides only what is specified.
     */
    public static final class Builder {

        private final Request request = new Request();

        public Builder id(String id) { request.id = id; return this; }
        public Builder name(String name) { request.name = name; return this; }
        public Builder method(HttpMethod method) { request.method = method; return this; }
        public Builder url(String url) { request.url = url; return this; }
        public Builder headers(List<Header> headers) { request.headers = headers; return this; }
        public Builder body(RequestBody body) { request.body = body; return this; }
        public Builder workspaceId(String workspaceId) { request.workspaceId = workspaceId; return this; }

        public Builder addHeader(String key, String value) {
            request.headers.add(new Header(key, value));
            return this;
        }

        public Builder addHeader(String key, String value, boolean enabled) {
            request.headers.add(new Header(key, value, enabled));
            return this;
        }

        public Request build() { return request; }
    }
}
