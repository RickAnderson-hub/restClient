package com.restclient.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The top-level container that groups related requests and environments —
 * equivalent to an Insomnia workspace or Postman collection root.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name"})
public final class Workspace {

    private String id;
    private String name;
    private String description;
    private List<Request> requests;
    private List<Environment> environments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Workspace() {
        this.id = UUID.randomUUID().toString();
        this.requests = new ArrayList<>();
        this.environments = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public Workspace(String name) {
        this();
        this.name = name;
    }
}
