package com.restclient.persistence.mapper;

import com.restclient.core.model.Environment;
import com.restclient.persistence.entity.EnvironmentEntity;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentMapperTest {

    @Test
    void toEntity_mapsAllFields() {
        // Arrange
        var env = new Environment("Staging", "ws-1");
        env.setId("env-1");
        env.setActive(true);
        env.setVariables(new LinkedHashMap<>(Map.of("BASE_URL", "https://staging.example.com")));
        // Act
        var entity = EnvironmentMapper.toEntity(env);
        // Assert
        assertEquals("env-1", entity.getId());
        assertEquals("ws-1", entity.getWorkspaceId());
        assertEquals("Staging", entity.getName());
        assertTrue(entity.isActive());
        assertEquals("https://staging.example.com", entity.getVariables().get("BASE_URL"));
    }

    @Test
    void toDomain_mapsAllFields() {
        // Arrange
        var entity = new EnvironmentEntity();
        entity.setId("env-2");
        entity.setWorkspaceId("ws-2");
        entity.setName("Production");
        entity.setActive(false);
        entity.setVariables(new LinkedHashMap<>(Map.of("API_KEY", "secret")));
        // Act
        var env = EnvironmentMapper.toDomain(entity);
        // Assert
        assertEquals("env-2", env.getId());
        assertEquals("ws-2", env.getWorkspaceId());
        assertEquals("Production", env.getName());
        assertFalse(env.isActive());
        assertEquals("secret", env.resolve("API_KEY"));
    }

    @Test
    void roundTrip_preservesVariables() {
        // Arrange
        var original = new Environment("Local", "ws-rt");
        original.getVariables().put("HOST", "localhost");
        original.getVariables().put("PORT", "8080");
        // Act
        var restored = EnvironmentMapper.toDomain(EnvironmentMapper.toEntity(original));
        // Assert
        assertEquals("localhost", restored.resolve("HOST"));
        assertEquals("8080", restored.resolve("PORT"));
    }

    @Test
    void toEntity_defensiveCopy_doesNotShareMap() {
        // Arrange
        var env = new Environment("X", "ws-1");
        env.getVariables().put("K", "V");
        // Act
        var entity = EnvironmentMapper.toEntity(env);
        entity.getVariables().put("K2", "V2");
        // Assert
        assertNull(env.resolve("K2"));
    }
}
