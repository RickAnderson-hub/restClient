package com.restclient.service;

import com.restclient.core.model.Environment;
import com.restclient.persistence.mapper.EnvironmentMapper;
import com.restclient.persistence.repository.EnvironmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for environment CRUD and active-environment management. At most one
 * environment per workspace should be active at any time; {@link #setActive}
 * ensures this invariant by deactivating all others in the same workspace.
 */
@Service
public class EnvironmentService {

    @Autowired
    private EnvironmentRepository environmentRepository;

    /** Persists the environment and returns the same instance. */
    public Environment save(Environment environment) {
        environmentRepository.save(EnvironmentMapper.toEntity(environment));
        return environment;
    }

    /** Returns all environments belonging to the given workspace. */
    public List<Environment> findByWorkspaceId(String workspaceId) {
        return environmentRepository.findByWorkspaceId(workspaceId)
                .stream().map(EnvironmentMapper::toDomain).toList();
    }

    /**
     * Returns the active environment for the given workspace, if any.
     * Used at send time to resolve {@code {{variable}}} placeholders.
     */
    public Optional<Environment> findActiveForWorkspace(String workspaceId) {
        return environmentRepository.findByWorkspaceIdAndActiveTrue(workspaceId)
                .map(EnvironmentMapper::toDomain);
    }

    /**
     * Makes the specified environment active and deactivates all others in the
     * same workspace. Only saves rows whose state actually changes to avoid
     * unnecessary dirty-writes.
     */
    @Transactional
    public void setActive(String environmentId, String workspaceId) {
        environmentRepository.findByWorkspaceId(workspaceId).forEach(e -> {
            var shouldBeActive = e.getId().equals(environmentId);
            if (e.isActive() != shouldBeActive) {
                e.setActive(shouldBeActive);
                environmentRepository.save(e);
            }
        });
    }

    /** Deletes the environment with the given id. */
    public void delete(String id) {
        environmentRepository.deleteById(id);
    }
}
