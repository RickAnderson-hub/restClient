package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTest {

    @Test
    void resolve_returnsValue_whenKeyExists() {
        // Arrange
        var env = new Environment("Staging", "ws-1");
        env.getVariables().put("BASE_URL", "https://staging.example.com");
        // Act
        var value = env.resolve("BASE_URL");
        // Assert
        assertEquals("https://staging.example.com", value);
    }

    @Test
    void resolve_returnsNull_whenKeyMissing() {
        // Arrange
        var env = new Environment("Staging", "ws-1");
        // Act
        var value = env.resolve("NONEXISTENT");
        // Assert
        assertNull(value);
    }

    @Test
    void noArgConstructor_setsIdAndEmptyVariables() {
        // Arrange + Act
        var env = new Environment();
        // Assert
        assertNotNull(env.getId());
        assertNotNull(env.getVariables());
    }

    @Test
    void twoArgConstructor_setsNameAndWorkspaceId() {
        // Arrange + Act
        var env = new Environment("Production", "ws-99");
        // Assert
        assertEquals("Production", env.getName());
        assertEquals("ws-99", env.getWorkspaceId());
    }

    @Test
    void equality_basedOnId() {
        // Arrange
        var a = new Environment();
        var b = new Environment();
        a.setId("env-1");
        b.setId("env-1");
        // Act + Assert
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void setVariables_replacesMap() {
        // Arrange
        var env = new Environment();
        // Act
        env.setVariables(Map.of("K", "V"));
        // Assert
        assertEquals("V", env.resolve("K"));
    }
}
