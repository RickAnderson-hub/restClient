package com.restclient.service;

import com.restclient.core.model.Request;
import com.restclient.persistence.mapper.RequestMapper;
import com.restclient.persistence.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for request CRUD operations. Each save stamps the updated timestamp
 * so the UI can show when a request was last modified.
 */
@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    /**
     * Persists the request, stamping the updated timestamp before saving.
     * Returns the same instance with the timestamp updated in place.
     */
    public Request save(Request request) {
        request.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(RequestMapper.toEntity(request));
        return request;
    }

    /** Returns the request with the given id, or empty if not found. */
    public Optional<Request> findById(String id) {
        return requestRepository.findById(id).map(RequestMapper::toDomain);
    }

    /** Returns all requests belonging to the given workspace. */
    public List<Request> findByWorkspaceId(String workspaceId) {
        return requestRepository.findByWorkspaceId(workspaceId)
                .stream().map(RequestMapper::toDomain).toList();
    }

    /** Deletes the request with the given id. */
    public void delete(String id) {
        requestRepository.deleteById(id);
    }
}
