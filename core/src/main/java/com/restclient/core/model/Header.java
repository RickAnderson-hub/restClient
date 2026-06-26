package com.restclient.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A single HTTP header key/value pair. The {@code enabled} flag lets the UI
 * keep a header in the list while excluding it from the outgoing request,
 * mirroring Insomnia's checkbox-per-row behavior.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public final class Header {

    private String key;
    private String value;
    private boolean enabled;

    public Header() {
        this.enabled = true;
    }

    public Header(String key, String value) {
        this(key, value, true);
    }

    public Header(String key, String value, boolean enabled) {
        this.key = key;
        this.value = value;
        this.enabled = enabled;
    }
}
