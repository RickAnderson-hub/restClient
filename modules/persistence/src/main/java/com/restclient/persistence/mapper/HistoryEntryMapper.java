package com.restclient.persistence.mapper;

import com.restclient.core.model.HistoryEntry;
import com.restclient.persistence.entity.HistoryEntryEntity;

/**
 * Converts between the {@link HistoryEntry} domain model and its JPA entity counterpart.
 * The request and response snapshots are passed through directly; their JSON
 * serialization is handled by JPA {@code AttributeConverter}s on the entity.
 */
public final class HistoryEntryMapper {

    private HistoryEntryMapper() {}

    /** Maps a domain history entry to its JPA entity. */
    public static HistoryEntryEntity toEntity(HistoryEntry entry) {
        var entity = new HistoryEntryEntity();
        entity.setId(entry.getId());
        entity.setWorkspaceId(entry.getWorkspaceId());
        entity.setTimestamp(entry.getTimestamp());
        entity.setRequestSnapshot(entry.getRequest());
        entity.setResponseSnapshot(entry.getResponse());
        return entity;
    }

    /** Maps a JPA entity back to a domain history entry. */
    public static HistoryEntry toDomain(HistoryEntryEntity entity) {
        var entry = new HistoryEntry();
        entry.setId(entity.getId());
        entry.setWorkspaceId(entity.getWorkspaceId());
        entry.setTimestamp(entity.getTimestamp());
        entry.setRequest(entity.getRequestSnapshot());
        entry.setResponse(entity.getResponseSnapshot());
        return entry;
    }
}
