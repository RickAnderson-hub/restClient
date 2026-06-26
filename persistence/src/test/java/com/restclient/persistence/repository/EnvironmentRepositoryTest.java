package com.restclient.persistence.repository;

import com.restclient.persistence.PersistenceTestApplication;
import com.restclient.persistence.entity.EnvironmentEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestApplication.class)
@Transactional
class EnvironmentRepositoryTest {

    @Autowired
    EnvironmentRepository environmentRepository;

    @Test
    void findByWorkspaceId_returnsOnlyMatchingEnvs() {
        // Arrange
        environmentRepository.save(env("e1", "ws-1", false));
        environmentRepository.save(env("e2", "ws-1", true));
        environmentRepository.save(env("e3", "ws-2", false));
        // Act
        var result = environmentRepository.findByWorkspaceId("ws-1");
        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findByWorkspaceIdAndActiveTrue_returnsActiveEnv() {
        // Arrange
        environmentRepository.save(env("e-inactive", "ws-act", false));
        environmentRepository.save(env("e-active", "ws-act", true));
        // Act
        var active = environmentRepository.findByWorkspaceIdAndActiveTrue("ws-act");
        // Assert
        assertTrue(active.isPresent());
        assertEquals("e-active", active.get().getId());
    }

    @Test
    void findByWorkspaceIdAndActiveTrue_empty_whenNoneActive() {
        // Arrange
        environmentRepository.save(env("e-all-off", "ws-empty", false));
        // Act
        var active = environmentRepository.findByWorkspaceIdAndActiveTrue("ws-empty");
        // Assert
        assertFalse(active.isPresent());
    }

    @Test
    void deleteByWorkspaceId_removesOnlyTargetEnvs() {
        // Arrange
        environmentRepository.save(env("del-1", "ws-del", false));
        environmentRepository.save(env("keep-1", "ws-keep", false));
        // Act
        environmentRepository.deleteByWorkspaceId("ws-del");
        // Assert
        assertTrue(environmentRepository.findByWorkspaceId("ws-del").isEmpty());
        assertEquals(1, environmentRepository.findByWorkspaceId("ws-keep").size());
    }

    @Test
    void saveWithVariables_roundTrip() {
        // Arrange
        var e = env("e-vars", "ws-1", false);
        e.getVariables().put("BASE_URL", "https://staging.example.com");
        e.getVariables().put("TOKEN", "secret");
        // Act
        environmentRepository.save(e);
        var loaded = environmentRepository.findById("e-vars").orElseThrow();
        // Assert
        assertEquals("https://staging.example.com", loaded.getVariables().get("BASE_URL"));
        assertEquals("secret", loaded.getVariables().get("TOKEN"));
    }

    private EnvironmentEntity env(String id, String workspaceId, boolean active) {
        var e = new EnvironmentEntity();
        e.setId(id);
        e.setWorkspaceId(workspaceId);
        e.setName("Env " + id);
        e.setActive(active);
        return e;
    }
}
