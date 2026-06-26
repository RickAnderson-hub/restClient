package com.restclient.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Embeddable value type for a single HTTP header. Stored via
 * {@code @ElementCollection} in the {@code request_headers} join table.
 * The {@code enabled} flag mirrors Insomnia's per-row checkbox behavior —
 * a disabled header stays in the list but is excluded from the outgoing request.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeaderEmbeddable {

    @Column(name = "header_key")
    private String key;
    @Column(name = "header_value")
    private String value;
    private boolean enabled;
}
