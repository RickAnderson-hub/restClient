package com.restclient.persistence.repository;

import com.restclient.persistence.entity.EnvironmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for {@link EnvironmentEntity}. Includes workspace-scoped
 * queries for listing environments and finding the currently active one.
 */
public interface EnvironmentRepository extends JpaRepository<EnvironmentEntity, String> {

    /** Returns all environments belonging to the given workspace. */
    List<EnvironmentEntity> findByWorkspaceId(String workspaceId);

    /**
     * Returns the active environment for the given workspace, if any.
     * At most one environment per workspace should have {@code active = true}.
     */
    Optional<EnvironmentEntity> findByWorkspaceIdAndActiveTrue(String workspaceId);

    /**
     * Deletes all environments in the given workspace. Called before deleting the
     * workspace itself since there is no cascade relationship defined at the JPA level.
     */
    void deleteByWorkspaceId(String workspaceId);
}
