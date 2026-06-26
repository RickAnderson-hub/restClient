package com.restclient.persistence.entity;

import com.restclient.core.model.Request;
import com.restclient.core.model.Response;
import com.restclient.persistence.converter.RequestJsonConverter;
import com.restclient.persistence.converter.ResponseJsonConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity that persists a {@link com.restclient.core.model.HistoryEntry}.
 * The request and response are stored as JSON blobs ({@code TEXT} columns) rather than
 * normalized rows, because history captures a point-in-time snapshot — the live
 * request may be edited after the fact, and history must not change with it.
 */
@Entity
@Table(name = "history_entries")
@Getter
@Setter
@NoArgsConstructor
public class HistoryEntryEntity {

    @Id
    private String id;
    private String workspaceId;
    private LocalDateTime timestamp;

    /** Full request snapshot serialized to JSON; restored as a {@link Request} on read. */
    @Convert(converter = RequestJsonConverter.class)
    @Column(name = "request_json", columnDefinition = "TEXT")
    private Request requestSnapshot;

    /** Full response snapshot serialized to JSON; restored as a {@link Response} on read. */
    @Convert(converter = ResponseJsonConverter.class)
    @Column(name = "response_json", columnDefinition = "TEXT")
    private Response responseSnapshot;
}
