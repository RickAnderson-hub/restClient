package com.restclient.persistence.converter;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restclient.core.model.Request;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA {@link AttributeConverter} that serializes a {@link Request} snapshot to/from
 * a JSON text column. Used in {@link com.restclient.persistence.entity.HistoryEntryEntity}
 * so history entries capture the exact request state at send time, independent of
 * any subsequent edits to the live request.
 *
 * <p>The {@code ObjectMapper} is a thread-safe singleton configured with
 * {@link JavaTimeModule} to handle {@code LocalDateTime} fields without relying on
 * timestamps, which are fragile across timezones.
 */
@Converter
public class RequestJsonConverter implements AttributeConverter<Request, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setVisibility(PropertyAccessor.ALL, Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

    @Override
    public String convertToDatabaseColumn(Request attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize Request to JSON", e);
        }
    }

    @Override
    public Request convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return MAPPER.readValue(dbData, Request.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize Request from JSON", e);
        }
    }
}
