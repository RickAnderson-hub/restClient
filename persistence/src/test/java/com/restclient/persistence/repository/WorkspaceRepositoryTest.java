package com.restclient.persistence.repository;

import com.restclient.persistence.PersistenceTestApplication;
import com.restclient.persistence.entity.WorkspaceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestApplication.class)
@Transactional
class WorkspaceRepositoryTest {

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Test
    void saveAndFindById_roundTrip() {
        // Arrange
        var ws = workspace("ws-1", "My API");
        // Act
        workspaceRepository.save(ws);
        var found = workspaceRepository.findById("ws-1");
        // Assert
        assertTrue(found.isPresent());
        assertEquals("My API", found.get().getName());
    }

    @Test
    void findAll_returnsAllSaved() {
        // Arrange
        workspaceRepository.save(workspace("ws-a", "Alpha"));
        workspaceRepository.save(workspace("ws-b", "Beta"));
        // Act
        var all = workspaceRepository.findAll();
        // Assert
        assertEquals(2, all.size());
    }

    @Test
    void deleteById_removesWorkspace() {
        // Arrange
        workspaceRepository.save(workspace("ws-del", "ToDelete"));
        // Act
        workspaceRepository.deleteById("ws-del");
        // Assert
        assertFalse(workspaceRepository.findById("ws-del").isPresent());
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        // Arrange (nothing)
        // Act
        var result = workspaceRepository.findById("nonexistent");
        // Assert
        assertFalse(result.isPresent());
    }

    private WorkspaceEntity workspace(String id, String name) {
        var e = new WorkspaceEntity();
        e.setId(id);
        e.setName(name);
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }
}
