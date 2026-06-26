package com.restclient.persistence.repository;

import com.restclient.persistence.entity.HistoryEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link HistoryEntryEntity}. Provides workspace-scoped
 * queries ordered by recency, used to populate the history panel.
 */
public interface HistoryEntryRepository extends JpaRepository<HistoryEntryEntity, String> {

    /**
     * Returns all history entries for the given workspace, newest first.
     * Used to populate the history panel in the UI.
     */
    List<HistoryEntryEntity> findByWorkspaceIdOrderByTimestampDesc(String workspaceId);

    /**
     * Deletes all history entries for the given workspace. Called before
     * deleting the workspace itself or when the user clears history.
     */
    void deleteByWorkspaceId(String workspaceId);
}
