package com.restclient.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA entity that persists a {@link com.restclient.core.model.Workspace} to the {@code workspaces} table.
 * Child requests and environments are stored in separate tables, joined via {@code workspace_id}.
 */
@Entity
@Table(name = "workspaces")
@Getter
@Setter
@NoArgsConstructor
public class WorkspaceEntity {

    @Id
    private String id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
