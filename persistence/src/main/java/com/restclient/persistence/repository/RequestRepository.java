package com.restclient.persistence.repository;

import com.restclient.persistence.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link RequestEntity}. Includes workspace-scoped
 * query and delete methods used by the service layer when cascading workspace operations.
 */
public interface RequestRepository extends JpaRepository<RequestEntity, String> {

    /**
     * Returns all requests belonging to the given workspace, in insertion order.
     */
    List<RequestEntity> findByWorkspaceId(String workspaceId);

    /**
     * Deletes all requests in the given workspace. Called before deleting the workspace
     * itself since there is no cascade relationship defined at the JPA level.
     */
    void deleteByWorkspaceId(String workspaceId);
}
