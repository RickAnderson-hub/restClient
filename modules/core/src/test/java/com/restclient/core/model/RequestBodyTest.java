package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestBodyTest {

    @Test
    void none_hasNullContentAndNoneType() {
        // Arrange + Act
        var body = RequestBody.none();
        // Assert
        assertEquals(BodyType.NONE, body.getType());
        assertNull(body.getContent());
        assertNull(body.getContentType());
    }

    @Test
    void getContentType_returnsTypeDefault_whenNoOverride() {
        // Arrange + Act
        var body = new RequestBody(BodyType.JSON, "{\"key\":\"value\"}");
        // Assert
        assertEquals("application/json", body.getContentType());
    }

    @Test
    void getContentType_returnsOverride_whenExplicitlySet() {
        // Arrange
        var body = new RequestBody(BodyType.JSON, "{}");
        // Act
        body.setContentType("application/vnd.custom+json");
        // Assert
        assertEquals("application/vnd.custom+json", body.getContentType());
    }

    @Test
    void getContentType_returnsNull_forNoneType() {
        // Arrange + Act
        var body = new RequestBody(BodyType.NONE, null);
        // Assert
        assertNull(body.getContentType());
    }

    @Test
    void noArgConstructor_defaultsToNone() {
        // Arrange + Act
        var body = new RequestBody();
        // Assert
        assertEquals(BodyType.NONE, body.getType());
    }

    @Test
    void equals_basedOnTypeContentAndContentType() {
        // Arrange
        var a = new RequestBody(BodyType.JSON, "{}");
        var b = new RequestBody(BodyType.JSON, "{}");
        // Act + Assert
        assertEquals(a, b);
    }

    @Test
    void notEquals_whenContentDiffers() {
        // Arrange
        var a = new RequestBody(BodyType.JSON, "{}");
        var b = new RequestBody(BodyType.JSON, "{\"x\":1}");
        // Act + Assert
        assertNotEquals(a, b);
    }
}
