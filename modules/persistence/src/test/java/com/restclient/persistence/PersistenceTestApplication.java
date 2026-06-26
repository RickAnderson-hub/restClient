package com.restclient.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal Spring Boot bootstrap for persistence module integration tests.
 * The component scan of this package picks up {@link com.restclient.persistence.config.PersistenceConfig},
 * which registers the entity and repository packages for JPA auto-configuration.
 */
@SpringBootApplication
public class PersistenceTestApplication {
}
