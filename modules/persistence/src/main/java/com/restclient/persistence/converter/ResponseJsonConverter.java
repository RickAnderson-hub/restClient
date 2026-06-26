package com.restclient.persistence.converter;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restclient.core.model.Response;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA {@link AttributeConverter} that serializes a {@link Response} snapshot to/from
 * a JSON text column. Mirrors {@link RequestJsonConverter} — see that class for
 * the rationale on JSON blobs vs. normalized rows for history data.
 */
@Converter
public class ResponseJsonConverter implements AttributeConverter<Response, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setVisibility(PropertyAccessor.ALL, Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

    @Override
    public String convertToDatabaseColumn(Response attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize Response to JSON", e);
        }
    }

    @Override
    public Response convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return MAPPER.readValue(dbData, Response.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize Response from JSON", e);
        }
    }
}
