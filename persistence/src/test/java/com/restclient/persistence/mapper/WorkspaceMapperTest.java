package com.restclient.persistence.mapper;

import com.restclient.core.model.Workspace;
import com.restclient.persistence.entity.WorkspaceEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceMapperTest {

    @Test
    void toEntity_mapsAllFields() {
        // Arrange
        var ws = new Workspace("Test WS");
        ws.setId("ws-1");
        ws.setDescription("A description");
        var now = LocalDateTime.now();
        ws.setCreatedAt(now);
        ws.setUpdatedAt(now);
        // Act
        var entity = WorkspaceMapper.toEntity(ws);
        // Assert
        assertEquals("ws-1", entity.getId());
        assertEquals("Test WS", entity.getName());
        assertEquals("A description", entity.getDescription());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void toDomain_mapsAllFields() {
        // Arrange
        var entity = new WorkspaceEntity();
        entity.setId("ws-2");
        entity.setName("My API");
        entity.setDescription("Desc");
        var now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        // Act
        var ws = WorkspaceMapper.toDomain(entity);
        // Assert
        assertEquals("ws-2", ws.getId());
        assertEquals("My API", ws.getName());
        assertEquals("Desc", ws.getDescription());
        assertEquals(now, ws.getCreatedAt());
    }

    @Test
    void toDomain_leavesListsEmpty() {
        // Arrange
        var entity = new WorkspaceEntity();
        entity.setId("ws-3");
        // Act
        var ws = WorkspaceMapper.toDomain(entity);
        // Assert
        assertTrue(ws.getRequests().isEmpty());
        assertTrue(ws.getEnvironments().isEmpty());
    }

    @Test
    void roundTrip_preservesFields() {
        // Arrange
        var original = new Workspace("RT WS");
        original.setId("ws-rt");
        original.setDescription("Round trip test");
        // Act
        var restored = WorkspaceMapper.toDomain(WorkspaceMapper.toEntity(original));
        // Assert
        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getDescription(), restored.getDescription());
    }
}
