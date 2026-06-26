package com.restclient.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * The payload of a request. {@code content} holds the raw text (JSON, XML,
 * form-encoded string, etc.); {@code contentType} may override the default
 * implied by {@code type}.
 */
@Getter
@Setter
@EqualsAndHashCode
public final class RequestBody {

    private BodyType type;
    private String content;
    private String contentType;

    public RequestBody() {
        this.type = BodyType.NONE;
    }

    public RequestBody(BodyType type, String content) {
        this.type = type;
        this.content = content;
        this.contentType = type != null ? type.getDefaultContentType() : null;
    }

    /** Convenience factory for an empty (no-payload) body. */
    public static RequestBody none() {
        return new RequestBody(BodyType.NONE, null);
    }

    /**
     * The effective Content-Type: the explicit override if set, otherwise the
     * default implied by {@link #getType()}.
     */
    public String getContentType() {
        if (contentType != null) {
            return contentType;
        }
        return type != null ? type.getDefaultContentType() : null;
    }
}
