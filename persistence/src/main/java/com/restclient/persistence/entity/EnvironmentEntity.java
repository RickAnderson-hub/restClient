package com.restclient.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JPA entity that persists a {@link com.restclient.core.model.Environment} to the {@code environments} table.
 * Variables are stored in a join table ({@code environment_variables}) as key/value string pairs,
 * preserving insertion order via a {@link LinkedHashMap}.
 */
@Entity
@Table(name = "environments")
@Getter
@Setter
@NoArgsConstructor
public class EnvironmentEntity {

    @Id
    private String id;
    private String workspaceId;
    private String name;

    /** True when this environment's variables are applied to outgoing requests in its workspace. */
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "environment_variables", joinColumns = @JoinColumn(name = "environment_id"))
    @MapKeyColumn(name = "var_key")
    @Column(name = "var_value", columnDefinition = "TEXT")
    private Map<String, String> variables = new LinkedHashMap<>();
}
