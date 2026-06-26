package com.restclient.persistence.repository;

import com.restclient.core.model.BodyType;
import com.restclient.core.model.HttpMethod;
import com.restclient.persistence.PersistenceTestApplication;
import com.restclient.persistence.entity.HeaderEmbeddable;
import com.restclient.persistence.entity.RequestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestApplication.class)
@Transactional
class RequestRepositoryTest {

    @Autowired
    RequestRepository requestRepository;

    @Test
    void findByWorkspaceId_returnsOnlyMatchingRequests() {
        // Arrange
        requestRepository.save(request("req-1", "ws-A"));
        requestRepository.save(request("req-2", "ws-A"));
        requestRepository.save(request("req-3", "ws-B"));
        // Act
        var forWsA = requestRepository.findByWorkspaceId("ws-A");
        // Assert
        assertEquals(2, forWsA.size());
    }

    @Test
    void deleteByWorkspaceId_removesOnlyTargetWorkspace() {
        // Arrange
        requestRepository.save(request("req-a", "ws-del"));
        requestRepository.save(request("req-b", "ws-keep"));
        // Act
        requestRepository.deleteByWorkspaceId("ws-del");
        // Assert
        assertTrue(requestRepository.findByWorkspaceId("ws-del").isEmpty());
        assertEquals(1, requestRepository.findByWorkspaceId("ws-keep").size());
    }

    @Test
    void saveWithHeaders_roundTrip() {
        // Arrange
        var req = request("req-h", "ws-1");
        req.getHeaders().add(new HeaderEmbeddable("Accept", "application/json", true));
        req.getHeaders().add(new HeaderEmbeddable("X-Skip", "true", false));
        // Act
        requestRepository.save(req);
        var loaded = requestRepository.findById("req-h").orElseThrow();
        // Assert
        assertEquals(2, loaded.getHeaders().size());
    }

    @Test
    void saveWithBody_roundTrip() {
        // Arrange
        var req = request("req-body", "ws-1");
        req.setBodyType(BodyType.JSON);
        req.setBodyContent("{\"x\":1}");
        req.setBodyContentType("application/json");
        // Act
        requestRepository.save(req);
        var loaded = requestRepository.findById("req-body").orElseThrow();
        // Assert
        assertEquals(BodyType.JSON, loaded.getBodyType());
        assertEquals("{\"x\":1}", loaded.getBodyContent());
    }

    private RequestEntity request(String id, String workspaceId) {
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
