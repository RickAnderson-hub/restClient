package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryEntryTest {

    @Test
    void noArgConstructor_setsIdAndTimestamp() {
        // Arrange + Act
        var entry = new HistoryEntry();
        // Assert
        assertNotNull(entry.getId());
        assertNotNull(entry.getTimestamp());
    }

    @Test
    void twoArgConstructor_linksRequestResponseAndWorkspace() {
        // Arrange
        var req = Request.builder().workspaceId("ws-1").build();
        var resp = new Response();
        resp.setStatusCode(200);
        // Act
        var entry = new HistoryEntry(req, resp);
        // Assert
        assertEquals(req, entry.getRequest());
        assertEquals(resp, entry.getResponse());
        assertEquals("ws-1", entry.getWorkspaceId());
    }

    @Test
    void twoArgConstructor_nullRequest_doesNotThrow() {
        // Arrange + Act
        var entry = new HistoryEntry(null, null);
        // Assert
        assertNull(entry.getWorkspaceId());
    }

    @Test
    void setters_updateFields() {
        // Arrange
        var entry = new HistoryEntry();
        // Act
        entry.setId("h-1");
        entry.setWorkspaceId("ws-1");
        // Assert
        assertEquals("h-1", entry.getId());
        assertEquals("ws-1", entry.getWorkspaceId());
    }
}
