package com.restclient.persistence.mapper;

import com.restclient.core.model.*;
import com.restclient.persistence.entity.HistoryEntryEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryEntryMapperTest {

    @Test
    void toEntity_mapsAllFields() {
        // Arrange
        var req = Request.builder().id("req-1").workspaceId("ws-1").build();
        var resp = new Response();
        resp.setStatusCode(200);
        var entry = new HistoryEntry(req, resp);
        entry.setId("h-1");
        // Act
        var entity = HistoryEntryMapper.toEntity(entry);
        // Assert
        assertEquals("h-1", entity.getId());
        assertEquals("ws-1", entity.getWorkspaceId());
        assertNotNull(entity.getTimestamp());
        assertSame(req, entity.getRequestSnapshot());
        assertSame(resp, entity.getResponseSnapshot());
    }

    @Test
    void toDomain_mapsAllFields() {
        // Arrange
        var req = Request.builder().id("req-2").build();
        var resp = new Response();
        var entity = new HistoryEntryEntity();
        entity.setId("h-2");
        entity.setWorkspaceId("ws-2");
        var ts = LocalDateTime.now();
        entity.setTimestamp(ts);
        entity.setRequestSnapshot(req);
        entity.setResponseSnapshot(resp);
        // Act
        var entry = HistoryEntryMapper.toDomain(entity);
        // Assert
        assertEquals("h-2", entry.getId());
        assertEquals("ws-2", entry.getWorkspaceId());
        assertEquals(ts, entry.getTimestamp());
        assertSame(req, entry.getRequest());
        assertSame(resp, entry.getResponse());
    }

    @Test
    void roundTrip_nullSnapshots_doNotThrow() {
        // Arrange
        var original = new HistoryEntry(null, null);
        original.setId("h-null");
        // Act
        var restored = HistoryEntryMapper.toDomain(HistoryEntryMapper.toEntity(original));
        // Assert
        assertNull(restored.getRequest());
        assertNull(restored.getResponse());
    }
}
