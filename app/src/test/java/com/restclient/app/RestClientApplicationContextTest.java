package com.restclient.app;

import com.restclient.core.model.Workspace;
import com.restclient.persistence.repository.EnvironmentRepository;
import com.restclient.persistence.repository.HistoryEntryRepository;
import com.restclient.persistence.repository.RequestRepository;
import com.restclient.persistence.repository.WorkspaceRepository;
import com.restclient.service.EnvironmentService;
import com.restclient.service.HistoryService;
import com.restclient.service.RequestService;
import com.restclient.service.WorkspaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end context test: verifies the full Spring application context loads
 * and all repository/service beans across modules are properly wired and
 * functional against the embedded H2 database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RestClientApplicationContextTest {

    @Autowired ApplicationContext context;
    @Autowired WorkspaceRepository workspaceRepository;
    @Autowired RequestRepository requestRepository;
    @Autowired EnvironmentRepository environmentRepository;
    @Autowired HistoryEntryRepository historyEntryRepository;
    @Autowired WorkspaceService workspaceService;
    @Autowired RequestService requestService;
    @Autowired EnvironmentService environmentService;
    @Autowired HistoryService historyService;

    @Test
    void contextLoads() {
        // Arrange (context started by @SpringBootTest)
        // Act (none — loading is the operation under test)
        // Assert
        assertNotNull(context);
    }

    @Test
    void allRepositoriesArePresentInContext() {
        // Arrange (context started by @SpringBootTest)
        // Act (none)
        // Assert
        assertNotNull(workspaceRepository);
        assertNotNull(requestRepository);
        assertNotNull(environmentRepository);
        assertNotNull(historyEntryRepository);
    }

    @Test
    void allServicesArePresentInContext() {
        // Arrange (context started by @SpringBootTest)
        // Act (none)
        // Assert
        assertNotNull(workspaceService);
        assertNotNull(requestService);
        assertNotNull(environmentService);
        assertNotNull(historyService);
    }

    @Test
    void workspaceRoundTrip_saveAndLoad() {
        // Arrange
        var ws = new Workspace("E2E Test Workspace");
        // Act
        workspaceService.save(ws);
        var loaded = workspaceService.findById(ws.getId());
        // Assert
        assertTrue(loaded.isPresent());
        assertEquals("E2E Test Workspace", loaded.get().getName());
        workspaceService.delete(ws.getId());
    }
}
