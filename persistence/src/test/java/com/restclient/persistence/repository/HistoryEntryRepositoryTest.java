package com.restclient.persistence.repository;

import com.restclient.core.model.Request;
import com.restclient.core.model.Response;
import com.restclient.persistence.PersistenceTestApplication;
import com.restclient.persistence.entity.HistoryEntryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestApplication.class)
@Transactional
class HistoryEntryRepositoryTest {

    @Autowired
    HistoryEntryRepository historyEntryRepository;

    @Test
    void findByWorkspaceIdOrderByTimestampDesc_newestFirst() {
        // Arrange
        historyEntryRepository.save(entry("h-1", "ws-1", LocalDateTime.now().minusSeconds(2)));
        historyEntryRepository.save(entry("h-2", "ws-1", LocalDateTime.now()));
        // Act
        var results = historyEntryRepository.findByWorkspaceIdOrderByTimestampDesc("ws-1");
        // Assert
        assertEquals(2, results.size());
        assertEquals("h-2", results.get(0).getId());
    }

    @Test
    void deleteByWorkspaceId_removesOnlyTargetEntries() {
        // Arrange
        historyEntryRepository.save(entry("h-del", "ws-del", LocalDateTime.now()));
        historyEntryRepository.save(entry("h-keep", "ws-keep", LocalDateTime.now()));
        // Act
        historyEntryRepository.deleteByWorkspaceId("ws-del");
        // Assert
        assertTrue(historyEntryRepository.findByWorkspaceIdOrderByTimestampDesc("ws-del").isEmpty());
        assertEquals(1, historyEntryRepository.findByWorkspaceIdOrderByTimestampDesc("ws-keep").size());
    }

    @Test
    void saveWithJsonSnapshots_roundTrip() {
        // Arrange
        var req = Request.builder().id("snap-req-1").name("GET users")
                .url("https://api.example.com/users").workspaceId("ws-snap").build();
        var resp = new Response();
        resp.setStatusCode(200);
        resp.setBody("[{\"id\":1}]");
        var entity = entry("h-snap", "ws-snap", LocalDateTime.now());
        entity.setRequestSnapshot(req);
        entity.setResponseSnapshot(resp);
        // Act
        historyEntryRepository.save(entity);
        var loaded = historyEntryRepository.findById("h-snap").orElseThrow();
        // Assert
        assertEquals("snap-req-1", loaded.getRequestSnapshot().getId());
        assertEquals(200, loaded.getResponseSnapshot().getStatusCode());
        assertEquals("[{\"id\":1}]", loaded.getResponseSnapshot().getBody());
    }

    @Test
    void saveWithNullSnapshots_doesNotThrow() {
        // Arrange
        var entity = entry("h-null", "ws-null", LocalDateTime.now());
        // Act
        historyEntryRepository.save(entity);
        var loaded = historyEntryRepository.findById("h-null").orElseThrow();
        // Assert
        assertNull(loaded.getRequestSnapshot());
        assertNull(loaded.getResponseSnapshot());
    }

    private HistoryEntryEntity entry(String id, String workspaceId, LocalDateTime timestamp) {
        var e = new HistoryEntryEntity();
        e.setId(id);
        e.setWorkspaceId(workspaceId);
        e.setTimestamp(timestamp);
        return e;
    }
}
