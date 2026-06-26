package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void noArgConstructor_setsAllDefaults() {
        // Arrange + Act
        var r = new Request();
        // Assert
        assertNotNull(r.getId());
        assertEquals(HttpMethod.GET, r.getMethod());
        assertNotNull(r.getHeaders());
        assertTrue(r.getHeaders().isEmpty());
        assertNotNull(r.getBody());
        assertNotNull(r.getCreatedAt());
        assertNotNull(r.getUpdatedAt());
    }

    @Test
    void builder_setsAllFields() {
        // Arrange + Act
        var r = Request.builder()
                .name("Get Users")
                .method(HttpMethod.GET)
                .url("https://api.example.com/users")
                .workspaceId("ws-1")
                .addHeader("Accept", "application/json")
                .body(new RequestBody(BodyType.JSON, "{}"))
                .build();
        // Assert
        assertEquals("Get Users", r.getName());
        assertEquals(HttpMethod.GET, r.getMethod());
        assertEquals("https://api.example.com/users", r.getUrl());
        assertEquals("ws-1", r.getWorkspaceId());
        assertEquals(1, r.getHeaders().size());
    }

    @Test
    void builder_headers_overrideReplacesAll() {
        // Arrange + Act
        var r = Request.builder()
                .headers(List.of(new Header("X-A", "1"), new Header("X-B", "2")))
                .build();
        // Assert
        assertEquals(2, r.getHeaders().size());
    }

    @Test
    void equality_basedOnId() {
        // Arrange
        var a = new Request();
        var b = new Request();
        a.setId("same-id");
        b.setId("same-id");
        // Act + Assert
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void notEquals_differentId() {
        // Arrange
        var a = new Request();
        var b = new Request();
        // Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void toString_containsMethodAndName() {
        // Arrange + Act
        var str = Request.builder().name("Test").method(HttpMethod.POST).url("http://x.com").build().toString();
        // Assert
        assertTrue(str.contains("POST"));
        assertTrue(str.contains("Test"));
    }
}
