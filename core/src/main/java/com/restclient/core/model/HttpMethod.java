package com.restclient.core.model;

/**
 * Supported HTTP methods for requests.
 */
public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS;

    /**
     * Whether this method conventionally carries a request body.
     */
    public boolean supportsBody() {
        return this == POST || this == PUT || this == PATCH || this == DELETE;
    }
}
