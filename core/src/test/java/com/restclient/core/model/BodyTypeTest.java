package com.restclient.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BodyTypeTest {

    @Test
    void noneHasNullContentType() {
        assertNull(BodyType.NONE.getDefaultContentType());
    }

    @Test
    void jsonContentType() {
        assertEquals("application/json", BodyType.JSON.getDefaultContentType());
    }

    @Test
    void xmlContentType() {
        assertEquals("application/xml", BodyType.XML.getDefaultContentType());
    }

    @Test
    void rawTextContentType() {
        assertEquals("text/plain", BodyType.RAW_TEXT.getDefaultContentType());
    }

    @Test
    void formUrlencodedContentType() {
        assertEquals("application/x-www-form-urlencoded", BodyType.FORM_URLENCODED.getDefaultContentType());
    }

    @Test
    void formDataContentType() {
        assertEquals("multipart/form-data", BodyType.FORM_DATA.getDefaultContentType());
    }

    @Test
    void binaryContentType() {
        assertEquals("application/octet-stream", BodyType.BINARY.getDefaultContentType());
    }
}
