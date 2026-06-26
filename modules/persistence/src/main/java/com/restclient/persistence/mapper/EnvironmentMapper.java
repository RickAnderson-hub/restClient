package com.restclient.persistence.mapper;

import com.restclient.core.model.Environment;
import com.restclient.persistence.entity.EnvironmentEntity;

import java.util.LinkedHashMap;

/**
 * Converts between the {@link Environment} domain model and its JPA entity counterpart.
 * Variable maps are defensively copied in both directions so entity and domain
 * instances never share mutable state.
 */
public final class EnvironmentMapper {

    private EnvironmentMapper() {}

    /** Maps a domain environment to its JPA entity. */
    public static EnvironmentEntity toEntity(Environment environment) {
        var entity = new EnvironmentEntity();
        entity.setId(environment.getId());
        entity.setWorkspaceId(environment.getWorkspaceId());
        entity.setName(environment.getName());
        entity.setActive(environment.isActive());
        entity.setVariables(new LinkedHashMap<>(environment.getVariables()));
        return entity;
    }

    /** Maps a JPA entity back to a domain environment. */
    public static Environment toDomain(EnvironmentEntity entity) {
        var environment = new Environment();
        environment.setId(entity.getId());
        environment.setWorkspaceId(entity.getWorkspaceId());
        environment.setName(entity.getName());
        environment.setActive(entity.isActive());
        environment.setVariables(new LinkedHashMap<>(entity.getVariables()));
        return environment;
    }
}
