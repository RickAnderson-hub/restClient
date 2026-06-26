package com.restclient.persistence.config;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Registers the persistence module's entity and repository packages with Spring Boot's
 * auto-configuration machinery.
 *
 * <p>In Spring Boot 4.x, {@code @EntityScan} was removed. The replacement is
 * {@link AutoConfigurationPackage}, which adds packages to the list that Spring Data JPA's
 * auto-configuration uses for entity scanning. Without this, the JPA entity manager would
 * only see {@code com.restclient.app} (the {@code @SpringBootApplication} package), and
 * entities in this persistence module would be invisible.
 */
@Configuration
@AutoConfigurationPackage(basePackages = {
        "com.restclient.persistence.entity",
        "com.restclient.persistence.converter"
})
@EnableJpaRepositories("com.restclient.persistence.repository")
public class PersistenceConfig {
}
