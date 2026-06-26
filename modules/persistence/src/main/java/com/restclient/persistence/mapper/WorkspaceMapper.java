package com.restclient.persistence.mapper;

import com.restclient.core.model.Workspace;
import com.restclient.persistence.entity.WorkspaceEntity;

/**
 * Converts between the {@link Workspace} domain model and its JPA entity counterpart.
 * Intentionally query-free: child collections ({@code requests}, {@code environments})
 * are left empty on domain conversion and populated by the service layer.
 */
public final class WorkspaceMapper {

    private WorkspaceMapper() {}

    /**
     * Maps a domain workspace to its JPA entity. Only the workspace's own scalar
     * fields are transferred; child lists are not persisted here.
     */
    public static WorkspaceEntity toEntity(Workspace workspace) {
        var entity = new WorkspaceEntity();
        entity.setId(workspace.getId());
        entity.setName(workspace.getName());
        entity.setDescription(workspace.getDescription());
        entity.setCreatedAt(workspace.getCreatedAt());
        entity.setUpdatedAt(workspace.getUpdatedAt());
        return entity;
    }

    /**
     * Maps a JPA entity back to a domain workspace. The returned workspace's
     * {@code requests} and {@code environments} lists are empty; the service
     * layer is responsible for populating them via separate repository queries.
     */
    public static Workspace toDomain(WorkspaceEntity entity) {
        var workspace = new Workspace();
        workspace.setId(entity.getId());
        workspace.setName(entity.getName());
        workspace.setDescription(entity.getDescription());
        workspace.setCreatedAt(entity.getCreatedAt());
        workspace.setUpdatedAt(entity.getUpdatedAt());
        return workspace;
    }
}
