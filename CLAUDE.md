# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build all modules
./gradlew build

# Run the desktop application
./gradlew :app:run

# Build without tests
./gradlew build -x test

# Run tests for a specific module
./gradlew :core:test
./gradlew :service:test

# Run a single test class
./gradlew :core:test --tests "com.restclient.core.model.HttpMethodTest"
```

JDK 25 is provisioned automatically via the foojay toolchain resolver — no local JDK setup required.

## Module Architecture

Five-module Gradle monorepo with a strict layered dependency graph:

```
app  ──► ui  ──► service  ──► persistence  ──► core
  └──────────────────────────────────────────► core
```

| Module        | Package prefix             | Role |
|---------------|----------------------------|------|
| `core`        | `com.restclient.core`      | Pure-POJO domain models. No Spring, no JavaFX. |
| `persistence` | `com.restclient.persistence` | Spring Data JPA + H2 entities and repositories. |
| `service`     | `com.restclient.service`   | Business logic, OkHttp HTTP execution, env interpolation. |
| `ui`          | `com.restclient.ui`        | JavaFX controllers, FXML views, ViewModels. |
| `app`         | `com.restclient.app`       | Entry point only — wires Spring Boot + JavaFX lifecycles. |

## Spring + JavaFX Lifecycle Bridge

This is the most non-obvious architectural point. JavaFX's `Application.launch()` is the process entry point (`RestClientApplication.main`), but the Spring context is started inside `JavaFxApplication.init()` (which runs before the FX thread). The `FXMLLoader` is then given Spring's bean factory as its controller factory:

```java
loader.setControllerFactory(applicationContext::getBean);
```

This means all `@Component`-annotated controllers (under `com.restclient.ui.controller`) are Spring-managed and receive full dependency injection. The component scan root (`scanBasePackages = "com.restclient"`) picks up beans across all modules automatically.

## Key Technology Versions

Defined in `gradle.properties`:
- Java / JavaFX: **25**
- Spring Boot: **4.1.0** (required for Gradle 9 compatibility — do not downgrade)
- Gradle wrapper: **9.6**
- OkHttp: 4.11.0
- H2: 2.2.220

Spring Boot 4.x requires Spring milestone repo (`https://repo.spring.io/milestone`), already configured in the root `build.gradle`.

## UI Layer Conventions

- FXML files: `ui/src/main/resources/fxml/`
- CSS (dark Insomnia-inspired theme): `ui/src/main/resources/css/main.css`
- Controllers are `@Component` Spring beans with `@FXML`-injected fields.
- The response environment variable interpolation syntax is `{{variableName}}`.

## Claude AI Integration

The Anthropic Java SDK (`com.anthropic:anthropic-java`) is used for AI features. Default model: `claude-opus-4-8` with `ThinkingConfigAdaptive` (adaptive thinking). Use streaming (`createStreaming()`) for any request that may produce long output, and dispatch UI updates via `Platform.runLater()` since Claude responses arrive on a non-FX thread.
