package com.restclient.core.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A point-in-time record pairing a sent request with the response it produced.
 * Powers the history panel and lets the user replay or inspect past calls.
 */
@Getter
@Setter
public final class HistoryEntry {

    private String id;
    private String workspaceId;
    private Request request;
    private Response response;
    private LocalDateTime timestamp;

    public HistoryEntry() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public HistoryEntry(Request request, Response response) {
        this();
        this.request = request;
        this.response = response;
        this.workspaceId = request != null ? request.getWorkspaceId() : null;
    }
}
