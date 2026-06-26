# PROJECT_CONTEXT.md

1. **Project Overview:**
- Multi-module Maven + Spring Boot repo used for Pact contract testing between **Courses** (provider) and **SpringBootRestService** (consumer).
- Courses exposes course catalog endpoints; SpringBootRestService exposes library/product endpoints that call Courses and are validated with Pact consumer tests.

2. **Tech Stack & Tooling:**
- Java: **21**
- Build: **Maven** (multi-module parent POM)
- Spring Boot: **2.5.2** (`spring-boot-starter-parent`)
- Spring MVC/REST: `spring-boot-starter-web`
- Data/ORM: `spring-boot-starter-data-jpa` (JPA repositories)
- Database drivers present in POMs:
  - MySQL: **8.0.19** (`mysql:mysql-connector-java:8.0.19`)
  - H2: present in SpringBootRestService (`com.h2database:h2`) 
- Pact (contract testing):
  - Provider JUnit5: `au.com.dius.pact.provider:junit5:4.2.1`
  - Consumer JUnit5: `au.com.dius.pact.consumer:junit5:4.2.7`
  - Pact Maven plugin: `au.com.dius.pact.provider:maven:4.1.11` (in `SpringBootRestService/pom.xml`)
  - Pact Broker URL + token configured in plugin (see module POM)
- Unit/integration testing: JUnit Jupiter **5.5.2** (Courses), Spring Boot test starter

3. **Architecture & Patterns:**
- Layering: Spring Boot **Controller → Repository/Service** (simple service layer in consumer module).
- State/data management:
  - Provider pact states use Spring Data repository operations inside `@State` methods to create/delete course records.
  - Consumer logic uses controller methods as integration-ish tests; outbound HTTP to provider is redirected to Pact `MockServer` by setting controller `baseUrl`.
- Contract testing pattern:
  - **Provider verification** via `@Provider` + `@PactFolder("pacts")` + `@TestTemplate` with `PactVerificationInvocationContextProvider`.
  - **Consumer pact definition** via `@Pact` methods with `PactDslWithProvider`, executed using `@PactTestFor` and `MockServer`.

4. **Directory Structure:**
- `./` (parent Maven aggregator)
- `Courses/`
  - `src/main/java/com/rahulshettyacademy/controller/` (course REST endpoints)
  - `src/main/java/com/rahulshettyacademy/repository/` (JPA repository)
  - `src/test/java/com/rahulshettyacademy/Courses/` (Pact provider test)
- `SpringBootRestService/`
  - `src/main/java/com/rahulshettyacademy/controller/` (library/product REST endpoints)
  - `src/main/java/com/rahulshettyacademy/repository/` (library persistence + custom repo)
  - `src/main/java/com/rahulshettyacademy/service/` (business logic)
  - `src/test/java/com/rahulshettyacademy/` (Pact consumer tests)

5. **Coding Conventions:**
- REST controllers:
  - Annotate with `@RestController` and use Spring mapping annotations (`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`).
  - For missing entities, throw `ResponseStatusException(HttpStatus.NOT_FOUND)`.
- Dependency wiring:
  - Prefer Spring DI (`@Autowired`) for repository/service/controller collaborators.
- Pact tests:
  - Provider side: define states using `@State(value=..., action=SETUP|TEARDOWN)`.
  - Provider verification uses `context.setTarget(new HttpTestTarget("localhost", port))` in `@BeforeEach`.
  - Consumer side: define contracts via `@Pact(consumer=...)` + `PactDslWithProvider` and validate using `@PactTestFor(pactMethod=..., port="9999")`.
  - When controller performs outbound HTTP, consumer tests must redirect that outbound dependency by setting the controller’s `baseUrl` to `mockServer.getUrl()`.

6. **Current State & Immediate Goals:**
- Working pieces (from current implementation):
  - Courses provider: `GET /allCourseDetails`, `GET /getCourseByName/{name}` returning `AllCourseData` or 404.
  - SpringBootRestService consumer/controller: `GET /getProductDetails/{name}` aggregates local library entity with remote course details from Courses; JSON is parsed and mapped into `SpecificProduct`.
  - Pact consumer tests validate:
    - `GET /allCourseDetails` response schema/count and presence of `price`.
    - `GET /getCourseByName/Appium` schema/body for success and 404-case message handling.
    - Exact computed JSON output for `ProductsPrices` and `SpecificProduct`.
- Immediate goals / bottlenecks:
  - Ensure Pact `pacts/` folder contents exist and match provider/consumer interaction names (`CoursesCatalogue`, `BooksCatalogue`).
  - Verify provider state hooks correctly align to Pact path params (e.g., deletion uses `repository.deleteById("Appium")` regardless of provided `name`).
  - Reduce fragility: consumer asserts exact JSON strings (serialization/order/model changes can break tests).

