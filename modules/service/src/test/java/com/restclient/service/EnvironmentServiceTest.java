package com.restclient.service;

import com.restclient.core.model.Environment;
import com.restclient.persistence.entity.EnvironmentEntity;
import com.restclient.persistence.repository.EnvironmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentServiceTest {

    @Mock EnvironmentRepository environmentRepository;
    @InjectMocks EnvironmentService service;

    @Test
    void save_persistsAndReturnsEnvironment() {
        // Arrange
        var env = new Environment("Staging", "ws-1");
        when(environmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        // Act
        var saved = service.save(env);
        // Assert
        verify(environmentRepository).save(any());
        assertEquals("Staging", saved.getName());
    }

    @Test
    void findByWorkspaceId_returnsMappedList() {
        // Arrange
        when(environmentRepository.findByWorkspaceId("ws-1"))
                .thenReturn(List.of(envEntity("e1", "ws-1", false), envEntity("e2", "ws-1", true)));
        // Act
        var result = service.findByWorkspaceId("ws-1");
        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findActiveForWorkspace_returnsActiveEnv() {
        // Arrange
        var entity = envEntity("e-active", "ws-1", true);
        when(environmentRepository.findByWorkspaceIdAndActiveTrue("ws-1")).thenReturn(Optional.of(entity));
        // Act
        var result = service.findActiveForWorkspace("ws-1");
        // Assert
        assertTrue(result.isPresent());
        assertTrue(result.get().isActive());
    }

    @Test
    void findActiveForWorkspace_returnsEmpty_whenNoneActive() {
        // Arrange
        when(environmentRepository.findByWorkspaceIdAndActiveTrue("ws-empty")).thenReturn(Optional.empty());
        // Act
        var result = service.findActiveForWorkspace("ws-empty");
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void setActive_activatesTargetAndDeactivatesOthers() {
        // Arrange
        var e1 = envEntity("e1", "ws-1", true);
        var e2 = envEntity("e2", "ws-1", false);
        when(environmentRepository.findByWorkspaceId("ws-1")).thenReturn(List.of(e1, e2));
        // Act
        service.setActive("e2", "ws-1");
        // Assert
        assertFalse(e1.isActive());
        assertTrue(e2.isActive());
        verify(environmentRepository, times(2)).save(any());
    }

    @Test
    void delete_delegatesToRepository() {
        // Arrange (nothing to set up)
        // Act
        service.delete("e-del");
        // Assert
        verify(environmentRepository).deleteById("e-del");
    }

    private EnvironmentEntity envEntity(String id, String workspaceId, boolean active) {
        var e = new EnvironmentEntity();
        e.setId(id);
        e.setWorkspaceId(workspaceId);
        e.setName("Env " + id);
        e.setActive(active);
        return e;
    }
}
