# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Technology Stack

- Java 21, Spring Boot 4, Vaadin 25
- Spring Data JPA with H2 in-memory database
- Maven build system

## Development Commands

```bash
./mvnw                           # Start in development mode (default goal: spring-boot:run)
./mvnw test                      # Run all tests
./mvnw test -Dtest=TaskServiceTest  # Run a single test class
./mvnw test -Dtest=TaskServiceTest#tasks_are_stored_in_the_database_with_the_current_timestamp  # Run a single test method
./mvnw -Pproduction package      # Build production JAR
```

The application runs at http://localhost:8080 and opens a browser automatically in dev mode.

## Architecture

Feature-based package structure under `com.example`:

- **`base.ui`**: Shared UI infrastructure
  - `MainLayout`: `@Layout`-annotated `AppLayout` with a drawer that auto-populates `SideNav` from `@Menu` annotations on views
  - `ViewToolbar`: Reusable toolbar `Composite` — takes a title and optional action `Component`s; use `ViewToolbar.group(...)` to wrap related buttons
- **`examplefeature`**: Reference feature — copy this structure for new features, then delete it when done

### Feature package template

Each feature contains:
- `MyEntity.java` — JPA entity; validation in setters; `equals`/`hashCode` based on ID only (hashCode returns `getClass().hashCode()` to avoid mutation issues)
- `MyRepository.java` — `JpaRepository` + `JpaSpecificationExecutor`; use `Slice<T>` (not `Page<T>`) when total count is not needed to avoid an extra COUNT query
- `MyService.java` — `@Service` with constructor injection; `@Transactional` on writes, `@Transactional(readOnly = true)` on reads
- `ui/MyView.java` — Vaadin Flow view; `@Route`, `@PageTitle`, `@Menu(order, icon, title)`; Grid lazy loading via `VaadinSpringDataHelpers.toSpringPageRequest(query)`

### Key patterns

- **Null safety**: `@Nullable` from `org.jspecify.annotations` (not `javax`/`jakarta`)
- **Sequences**: Entities use `@GeneratedValue(strategy = GenerationType.SEQUENCE)`
- **Schema**: `spring.jpa.hibernate.ddl-auto=update` in dev — use Flyway in production
- **Vaadin packages**: `vaadin.allowed-packages` in `application.properties` must include any new top-level package you add

## Testing

Two test patterns are used:

**Service tests** (`@SpringBootTest`, `@Transactional`): Spring context, real H2 database, `@Autowired` injection. Transactions roll back after each test.

**View tests** (`SpringBrowserlessTest`): Headless Vaadin UI tests from `vaadin:browserless-test-junit6`. Navigate to a view with `navigate(MyView.class)`. Access UI components via `test(view.field)` — **fields in the view class must be package-private** (not `private`) for the test to access them. Use `$(ComponentType.class)` to query rendered components.

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class MyViewTest extends SpringBrowserlessTest {
    @Test
    void example() {
        var view = navigate(MyView.class);
        test(view.myGrid).size();        // interact with grid
        test(view.myButton).click();     // click button
        $(Notification.class).single();  // assert notification appeared
    }
}
```
