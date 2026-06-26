package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeaderTest {

    @Test
    void noArgConstructor_enabledByDefault() {
        // Arrange + Act
        var h = new Header();
        // Assert
        assertTrue(h.isEnabled());
    }

    @Test
    void twoArgConstructor_enabledByDefault() {
        // Arrange + Act
        var h = new Header("Content-Type", "application/json");
        // Assert
        assertEquals("Content-Type", h.getKey());
        assertEquals("application/json", h.getValue());
        assertTrue(h.isEnabled());
    }

    @Test
    void threeArgConstructor_respectsEnabledFlag() {
        // Arrange + Act
        var h = new Header("X-Skip", "true", false);
        // Assert
        assertFalse(h.isEnabled());
    }

    @Test
    void equals_sameKeyValueEnabled() {
        // Arrange
        var a = new Header("Accept", "*/*", true);
        var b = new Header("Accept", "*/*", true);
        // Act + Assert
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void notEquals_differentKey() {
        // Arrange (nothing)
        // Act + Assert
        assertNotEquals(new Header("A", "v"), new Header("B", "v"));
    }

    @Test
    void toString_containsKeyAndValue() {
        // Arrange + Act
        var str = new Header("X-Foo", "bar").toString();
        // Assert
        assertTrue(str.contains("X-Foo"));
        assertTrue(str.contains("bar"));
    }
}
