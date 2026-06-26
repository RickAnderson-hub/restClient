package com.restclient.service;

import com.restclient.core.model.Workspace;
import com.restclient.persistence.entity.WorkspaceEntity;
import com.restclient.persistence.repository.EnvironmentRepository;
import com.restclient.persistence.repository.HistoryEntryRepository;
import com.restclient.persistence.repository.RequestRepository;
import com.restclient.persistence.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Mock WorkspaceRepository workspaceRepository;
    @Mock RequestRepository requestRepository;
    @Mock EnvironmentRepository environmentRepository;
    @Mock HistoryEntryRepository historyEntryRepository;
    @InjectMocks WorkspaceService service;

    @Test
    void findAll_returnsAllWorkspaces() {
        // Arrange
        when(workspaceRepository.findAll()).thenReturn(List.of(wsEntity("ws-1", "Alpha"), wsEntity("ws-2", "Beta")));
        when(requestRepository.findByWorkspaceId(any())).thenReturn(List.of());
        when(environmentRepository.findByWorkspaceId(any())).thenReturn(List.of());
        // Act
        var result = service.findAll();
        // Assert
        assertEquals(2, result.size());
        assertEquals("Alpha", result.get(0).getName());
    }

    @Test
    void findById_returnsPopulatedWorkspace_whenFound() {
        // Arrange
        when(workspaceRepository.findById("ws-1")).thenReturn(Optional.of(wsEntity("ws-1", "My WS")));
        when(requestRepository.findByWorkspaceId("ws-1")).thenReturn(List.of());
        when(environmentRepository.findByWorkspaceId("ws-1")).thenReturn(List.of());
        // Act
        var result = service.findById("ws-1");
        // Assert
        assertTrue(result.isPresent());
        assertEquals("My WS", result.get().getName());
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        // Arrange
        when(workspaceRepository.findById("missing")).thenReturn(Optional.empty());
        // Act
        var result = service.findById("missing");
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void save_persistsAndReturnsWorkspace() {
        // Arrange
        var ws = new Workspace("New WS");
        ws.setId("ws-new");
        when(workspaceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        // Act
        var saved = service.save(ws);
        // Assert
        verify(workspaceRepository).save(any());
        assertEquals("New WS", saved.getName());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void delete_cascadesToAllChildEntities() {
        // Arrange (nothing to set up)
        // Act
        service.delete("ws-del");
        // Assert
        verify(historyEntryRepository).deleteByWorkspaceId("ws-del");
        verify(requestRepository).deleteByWorkspaceId("ws-del");
        verify(environmentRepository).deleteByWorkspaceId("ws-del");
        verify(workspaceRepository).deleteById("ws-del");
    }

    private WorkspaceEntity wsEntity(String id, String name) {
        var e = new WorkspaceEntity();
        e.setId(id);
        e.setName(name);
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }
}
