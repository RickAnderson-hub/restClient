package com.restclient.persistence.repository;

import com.restclient.persistence.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link WorkspaceEntity}. Provides standard CRUD
 * operations and paging via {@link JpaRepository}.
 */
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, String> {
}
