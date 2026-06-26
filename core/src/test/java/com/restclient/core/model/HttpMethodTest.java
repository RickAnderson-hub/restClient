package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpMethodTest {

    @Test
    void supportsBody_trueForMutatingMethods() {
        // Arrange (nothing)
        // Act + Assert
        assertTrue(HttpMethod.POST.supportsBody());
        assertTrue(HttpMethod.PUT.supportsBody());
        assertTrue(HttpMethod.PATCH.supportsBody());
        assertTrue(HttpMethod.DELETE.supportsBody());
    }

    @Test
    void supportsBody_falseForReadMethods() {
        // Arrange (nothing)
        // Act + Assert
        assertFalse(HttpMethod.GET.supportsBody());
        assertFalse(HttpMethod.HEAD.supportsBody());
        assertFalse(HttpMethod.OPTIONS.supportsBody());
    }

    @Test
    void allValuesAreDeclared() {
        // Arrange (nothing)
        // Act
        var count = HttpMethod.values().length;
        // Assert
        assertEquals(7, count);
    }
}
