package com.restclient.service;

import com.restclient.core.model.HistoryEntry;
import com.restclient.persistence.mapper.HistoryEntryMapper;
import com.restclient.persistence.repository.HistoryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for persisting and querying request/response history. Entries are
 * always returned newest-first to match the expected UI display order.
 */
@Service
public class HistoryService {

    @Autowired
    private HistoryEntryRepository historyEntryRepository;

    /**
     * Saves a history entry (typically called immediately after an HTTP request
     * completes). Returns the same instance for fluent use.
     */
    public HistoryEntry save(HistoryEntry entry) {
        historyEntryRepository.save(HistoryEntryMapper.toEntity(entry));
        return entry;
    }

    /**
     * Returns all history entries for the given workspace, newest first.
     */
    public List<HistoryEntry> findByWorkspaceId(String workspaceId) {
        return historyEntryRepository.findByWorkspaceIdOrderByTimestampDesc(workspaceId)
                .stream().map(HistoryEntryMapper::toDomain).toList();
    }

    /** Deletes a single history entry by id. */
    public void delete(String id) {
        historyEntryRepository.deleteById(id);
    }

    /** Deletes all history for the given workspace. */
    public void clearWorkspaceHistory(String workspaceId) {
        historyEntryRepository.deleteByWorkspaceId(workspaceId);
    }
}
