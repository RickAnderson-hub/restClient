package com.restclient.persistence.mapper;

import com.restclient.core.model.Header;
import com.restclient.core.model.Request;
import com.restclient.core.model.RequestBody;
import com.restclient.persistence.entity.HeaderEmbeddable;
import com.restclient.persistence.entity.RequestEntity;

import java.util.List;

/**
 * Converts between the {@link Request} domain model and its JPA entity counterpart.
 * Headers are mapped to/from {@link HeaderEmbeddable} instances. The body is
 * flattened into three scalar fields on the entity side.
 */
public final class RequestMapper {

    private RequestMapper() {}

    /**
     * Maps a domain request to its JPA entity, including headers and body fields.
     * The effective content-type (override or type default) is stored so it round-trips correctly.
     */
    public static RequestEntity toEntity(Request request) {
        var entity = new RequestEntity();
        entity.setId(request.getId());
        entity.setName(request.getName());
        entity.setMethod(request.getMethod());
        entity.setUrl(request.getUrl());
        entity.setWorkspaceId(request.getWorkspaceId());
        entity.setCreatedAt(request.getCreatedAt());
        entity.setUpdatedAt(request.getUpdatedAt());
        var embeddables = request.getHeaders().stream()
                .map(h -> new HeaderEmbeddable(h.getKey(), h.getValue(), h.isEnabled()))
                .toList();
        entity.setHeaders(embeddables);
        var body = request.getBody();
        if (body != null) {
            entity.setBodyType(body.getType());
            entity.setBodyContent(body.getContent());
            entity.setBodyContentType(body.getContentType());
        }
        return entity;
    }

    /** Maps a JPA entity back to a domain request. */
    public static Request toDomain(RequestEntity entity) {
        var request = new Request();
        request.setId(entity.getId());
        request.setName(entity.getName());
        request.setMethod(entity.getMethod());
        request.setUrl(entity.getUrl());
        request.setWorkspaceId(entity.getWorkspaceId());
        request.setCreatedAt(entity.getCreatedAt());
        request.setUpdatedAt(entity.getUpdatedAt());
        List<Header> headers = entity.getHeaders().stream()
                .map(h -> new Header(h.getKey(), h.getValue(), h.isEnabled()))
                .toList();
        request.setHeaders(headers);
        var body = new RequestBody();
        body.setType(entity.getBodyType());
        body.setContent(entity.getBodyContent());
        body.setContentType(entity.getBodyContentType());
        request.setBody(body);
        return request;
    }
}
