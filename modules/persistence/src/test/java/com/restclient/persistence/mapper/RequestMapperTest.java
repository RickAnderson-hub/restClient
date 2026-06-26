package com.restclient.persistence.mapper;

import com.restclient.core.model.*;
import com.restclient.persistence.entity.RequestEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {

    @Test
    void toEntity_mapsAllScalarFields() {
        // Arrange
        var req = Request.builder()
                .id("req-1").name("Create User").method(HttpMethod.POST)
                .url("https://api.example.com/users").workspaceId("ws-1").build();
        var now = LocalDateTime.now();
        req.setCreatedAt(now);
        req.setUpdatedAt(now);
        // Act
        var entity = RequestMapper.toEntity(req);
        // Assert
        assertEquals("req-1", entity.getId());
        assertEquals("Create User", entity.getName());
        assertEquals(HttpMethod.POST, entity.getMethod());
        assertEquals("https://api.example.com/users", entity.getUrl());
        assertEquals("ws-1", entity.getWorkspaceId());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    void toEntity_mapsHeaders() {
        // Arrange
        var req = Request.builder()
                .addHeader("Accept", "application/json")
                .addHeader("X-Token", "abc", false)
                .build();
        // Act
        var entity = RequestMapper.toEntity(req);
        // Assert
        assertEquals(2, entity.getHeaders().size());
        assertTrue(entity.getHeaders().get(0).isEnabled());
        assertFalse(entity.getHeaders().get(1).isEnabled());
    }

    @Test
    void toEntity_mapsJsonBody() {
        // Arrange
        var req = Request.builder().body(new RequestBody(BodyType.JSON, "{\"name\":\"Alice\"}")).build();
        // Act
        var entity = RequestMapper.toEntity(req);
        // Assert
        assertEquals(BodyType.JSON, entity.getBodyType());
        assertEquals("{\"name\":\"Alice\"}", entity.getBodyContent());
        assertEquals("application/json", entity.getBodyContentType());
    }

    @Test
    void roundTrip_preservesAllFields() {
        // Arrange
        var original = Request.builder()
                .id("req-rt").name("RT").method(HttpMethod.PUT).url("http://x.com").workspaceId("ws-rt")
                .addHeader("Content-Type", "application/json")
                .body(new RequestBody(BodyType.JSON, "{}"))
                .build();
        // Act
        var restored = RequestMapper.toDomain(RequestMapper.toEntity(original));
        // Assert
        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getMethod(), restored.getMethod());
        assertEquals(original.getUrl(), restored.getUrl());
        assertEquals(1, restored.getHeaders().size());
        assertEquals("Content-Type", restored.getHeaders().get(0).getKey());
        assertEquals(BodyType.JSON, restored.getBody().getType());
    }

    @Test
    void toEntity_noneBody_storesNullBodyContent() {
        // Arrange
        var req = new Request();
        // Act
        var entity = RequestMapper.toEntity(req);
        // Assert
        assertEquals(BodyType.NONE, entity.getBodyType());
        assertNull(entity.getBodyContent());
    }
}
