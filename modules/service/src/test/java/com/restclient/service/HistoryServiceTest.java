package com.restclient.service;

import com.restclient.core.model.HistoryEntry;
import com.restclient.core.model.Request;
import com.restclient.core.model.Response;
import com.restclient.persistence.entity.HistoryEntryEntity;
import com.restclient.persistence.repository.HistoryEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock HistoryEntryRepository historyEntryRepository;
    @InjectMocks HistoryService service;

    @Test
    void save_persistsAndReturnsEntry() {
        // Arrange
        var entry = new HistoryEntry(Request.builder().workspaceId("ws-1").build(), new Response());
        when(historyEntryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        // Act
        var saved = service.save(entry);
        // Assert
        verify(historyEntryRepository).save(any());
        assertSame(entry, saved);
    }

    @Test
    void findByWorkspaceId_returnsMappedEntriesNewestFirst() {
        // Arrange
        when(historyEntryRepository.findByWorkspaceIdOrderByTimestampDesc("ws-1"))
                .thenReturn(List.of(historyEntity("h2", "ws-1"), historyEntity("h1", "ws-1")));
        // Act
        var result = service.findByWorkspaceId("ws-1");
        // Assert
        assertEquals(2, result.size());
        assertEquals("h2", result.get(0).getId());
    }

    @Test
    void delete_delegatesToRepository() {
        // Arrange (nothing to set up)
        // Act
        service.delete("h-del");
        // Assert
        verify(historyEntryRepository).deleteById("h-del");
    }

    @Test
    void clearWorkspaceHistory_deletesAllForWorkspace() {
        // Arrange (nothing to set up)
        // Act
        service.clearWorkspaceHistory("ws-clear");
        // Assert
        verify(historyEntryRepository).deleteByWorkspaceId("ws-clear");
    }

    private HistoryEntryEntity historyEntity(String id, String workspaceId) {
        var e = new HistoryEntryEntity();
        e.setId(id);
        e.setWorkspaceId(workspaceId);
        e.setTimestamp(LocalDateTime.now());
        return e;
    }
}
