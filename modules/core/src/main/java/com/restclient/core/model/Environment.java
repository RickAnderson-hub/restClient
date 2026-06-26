package com.restclient.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A named set of variables (e.g. "Local", "Staging", "Production") scoped to a
 * workspace. Variables are referenced in requests via {@code {{name}}} syntax
 * and resolved at send time.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public final class Environment {

    private String id;
    private String workspaceId;
    private String name;
    private Map<String, String> variables;
    private boolean active;

    public Environment() {
        this.id = UUID.randomUUID().toString();
        this.variables = new LinkedHashMap<>();
    }

    public Environment(String name, String workspaceId) {
        this();
        this.name = name;
        this.workspaceId = workspaceId;
    }

    /**
     * Looks up a variable value by key, or returns {@code null} if undefined.
     */
    public String resolve(String key) {
        return variables.get(key);
    }
}
