package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void isSuccessful_trueFor2xx() {
        // Arrange
        var r = new Response();
        // Act + Assert
        r.setStatusCode(200); assertTrue(r.isSuccessful());
        r.setStatusCode(201); assertTrue(r.isSuccessful());
        r.setStatusCode(299); assertTrue(r.isSuccessful());
    }

    @Test
    void isSuccessful_falseFor3xx4xx5xx() {
        // Arrange
        var r = new Response();
        // Act + Assert
        r.setStatusCode(301); assertFalse(r.isSuccessful());
        r.setStatusCode(400); assertFalse(r.isSuccessful());
        r.setStatusCode(500); assertFalse(r.isSuccessful());
    }

    @Test
    void noArgConstructor_initializesIdAndTimestampAndHeaders() {
        // Arrange + Act
        var r = new Response();
        // Assert
        assertNotNull(r.getId());
        assertNotNull(r.getTimestamp());
        assertNotNull(r.getHeaders());
    }

    @Test
    void toString_containsStatusCode() {
        // Arrange
        var r = new Response();
        r.setStatusCode(200);
        r.setStatusText("OK");
        // Act
        var str = r.toString();
        // Assert
        assertTrue(str.contains("200"));
    }
}
