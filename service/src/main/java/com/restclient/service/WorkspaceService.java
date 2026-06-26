package com.restclient.service;

import com.restclient.core.model.Environment;
import com.restclient.core.model.Request;
import com.restclient.core.model.Workspace;
import com.restclient.persistence.entity.WorkspaceEntity;
import com.restclient.persistence.mapper.EnvironmentMapper;
import com.restclient.persistence.mapper.RequestMapper;
import com.restclient.persistence.mapper.WorkspaceMapper;
import com.restclient.persistence.repository.EnvironmentRepository;
import com.restclient.persistence.repository.HistoryEntryRepository;
import com.restclient.persistence.repository.RequestRepository;
import com.restclient.persistence.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for workspace lifecycle operations. Treats the workspace as the root
 * aggregate: loading a workspace also fetches its requests and environments,
 * and deleting a workspace cascades to all child entities.
 */
@Service
public class WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private EnvironmentRepository environmentRepository;
    @Autowired
    private HistoryEntryRepository historyEntryRepository;

    /** Returns all workspaces, each fully populated with requests and environments. */
    public List<Workspace> findAll() {
        return workspaceRepository.findAll().stream()
                .map(this::loadFull)
                .toList();
    }

    /**
     * Returns the workspace with the given id, fully populated with requests and
     * environments, or empty if no workspace exists for that id.
     */
    public Optional<Workspace> findById(String id) {
        return workspaceRepository.findById(id).map(this::loadFull);
    }

    /**
     * Persists the workspace's own scalar fields and stamps the updated timestamp.
     * Child requests and environments are managed independently via {@link RequestService}
     * and {@link EnvironmentService}.
     */
    @Transactional
    public Workspace save(Workspace workspace) {
        workspace.setUpdatedAt(LocalDateTime.now());
        workspaceRepository.save(WorkspaceMapper.toEntity(workspace));
        return workspace;
    }

    /**
     * Deletes the workspace and all of its child requests, environments, and
     * history entries. Deletion order matters: children before parent to avoid FK violations.
     */
    @Transactional
    public void delete(String id) {
        historyEntryRepository.deleteByWorkspaceId(id);
        requestRepository.deleteByWorkspaceId(id);
        environmentRepository.deleteByWorkspaceId(id);
        workspaceRepository.deleteById(id);
    }

    private Workspace loadFull(WorkspaceEntity entity) {
        var workspace = WorkspaceMapper.toDomain(entity);
        var requests = requestRepository.findByWorkspaceId(entity.getId())
                .stream().map(RequestMapper::toDomain).toList();
        var environments = environmentRepository.findByWorkspaceId(entity.getId())
                .stream().map(EnvironmentMapper::toDomain).toList();
        workspace.setRequests(requests);
        workspace.setEnvironments(environments);
        return workspace;
    }
}
