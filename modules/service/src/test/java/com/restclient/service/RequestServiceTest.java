package com.restclient.service;

import com.restclient.core.model.HttpMethod;
import com.restclient.core.model.Request;
import com.restclient.persistence.entity.RequestEntity;
import com.restclient.persistence.repository.RequestRepository;
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
class RequestServiceTest {

    @Mock RequestRepository requestRepository;
    @InjectMocks RequestService service;

    @Test
    void save_setsUpdatedAtAndPersists() {
        // Arrange
        var req = Request.builder().id("r1").method(HttpMethod.GET).url("http://x.com").workspaceId("ws-1").build();
        when(requestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        // Act
        var saved = service.save(req);
        // Assert
        assertNotNull(saved.getUpdatedAt());
        verify(requestRepository).save(any());
    }

    @Test
    void findById_returnsMappedRequest_whenFound() {
        // Arrange
        var entity = reqEntity("r1", "ws-1");
        when(requestRepository.findById("r1")).thenReturn(Optional.of(entity));
        // Act
        var result = service.findById("r1");
        // Assert
        assertTrue(result.isPresent());
        assertEquals("r1", result.get().getId());
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        // Arrange
        when(requestRepository.findById("missing")).thenReturn(Optional.empty());
        // Act
        var result = service.findById("missing");
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByWorkspaceId_returnsMappedList() {
        // Arrange
        when(requestRepository.findByWorkspaceId("ws-1"))
                .thenReturn(List.of(reqEntity("r1", "ws-1"), reqEntity("r2", "ws-1")));
        // Act
        var result = service.findByWorkspaceId("ws-1");
        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void delete_delegatesToRepository() {
        // Arrange (nothing to set up)
        // Act
        service.delete("r-del");
        // Assert
        verify(requestRepository).deleteById("r-del");
    }

    private RequestEntity reqEntity(String id, String workspaceId) {
        var e = new RequestEntity();
        e.setId(id);
        e.setName("Request " + id);
        e.setMethod(HttpMethod.GET);
        e.setUrl("https://example.com");
        e.setWorkspaceId(workspaceId);
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }
}
