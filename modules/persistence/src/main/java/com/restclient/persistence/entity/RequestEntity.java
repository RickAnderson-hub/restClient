package com.restclient.persistence.entity;

import com.restclient.core.model.BodyType;
import com.restclient.core.model.HttpMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity that persists a {@link com.restclient.core.model.Request} to the {@code requests} table.
 * Headers are stored as an {@code @ElementCollection} in {@code request_headers}.
 * The body is flattened into three columns (type, content, content-type) rather than a
 * separate table, since it is always fetched with the request.
 */
@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
public class RequestEntity {

    @Id
    private String id;
    private String name;
    @Enumerated(EnumType.STRING)
    private HttpMethod method;
    @Column(columnDefinition = "TEXT")
    private String url;
    private String workspaceId;

    /** Headers kept in a join table; EAGER so they load with the request in one round-trip. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "request_headers", joinColumns = @JoinColumn(name = "request_id"))
    private List<HeaderEmbeddable> headers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "body_type")
    private BodyType bodyType;
    @Column(name = "body_content", columnDefinition = "TEXT")
    private String bodyContent;
    @Column(name = "body_content_type")
    private String bodyContentType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
