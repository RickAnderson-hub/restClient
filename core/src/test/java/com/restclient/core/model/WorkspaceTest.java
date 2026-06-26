package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceTest {

    @Test
    void noArgConstructor_setsAllDefaults() {
        // Arrange + Act
        var ws = new Workspace();
        // Assert
        assertNotNull(ws.getId());
        assertNotNull(ws.getRequests());
        assertNotNull(ws.getEnvironments());
        assertNotNull(ws.getCreatedAt());
    }

    @Test
    void nameConstructor_setsName() {
        // Arrange + Act
        var ws = new Workspace("My API");
        // Assert
        assertEquals("My API", ws.getName());
    }

    @Test
    void equality_basedOnId() {
        // Arrange
        var a = new Workspace();
        var b = new Workspace();
        a.setId("ws-1");
        b.setId("ws-1");
        // Act + Assert
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void notEquals_differentIds() {
        // Arrange
        var a = new Workspace();
        var b = new Workspace();
        // Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void toString_containsId() {
        // Arrange
        var ws = new Workspace("Test");
        ws.setId("fixed-id");
        // Act
        var str = ws.toString();
        // Assert
        assertTrue(str.contains("fixed-id"));
    }
}
