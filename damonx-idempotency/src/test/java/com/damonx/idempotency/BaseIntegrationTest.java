package com.damonx.idempotency;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for integration tests that require a PostgreSQL database.
 * <p>
 * This class leverages Testcontainers to provision a shared PostgreSQL instance
 * that is started once and reused across all inheriting test suites. By using
 * static initialization, we avoid repeatedly starting new containers, which
 * significantly improves test execution performance.
 * </p>
 *
 * <p>
 * The {@link PostgreSQLContainer} instance is automatically registered as a Spring
 * {@code DataSource} via {@link org.springframework.boot.testcontainers.service.connection.ServiceConnection},
 * allowing Spring Boot tests to connect to it without additional configuration.
 * </p>
 *
 * <p>
 * The {@link org.springframework.test.context.jdbc.SqlConfig} annotation is set to
 * {@code ISOLATED}, ensuring that each test class runs with its own database transaction
 * boundaries, preventing unintended cross-test data leakage.
 * </p>
 *
 * <p>
 * Any test class extending {@code BaseIntegrationTest} will automatically:
 * </p>
 * <ul>
 *     <li>Start and share a single PostgreSQL Testcontainer instance</li>
 *     <li>Use the container as the test database</li>
 *     <li>Benefit from isolated SQL execution per test class</li>
 * </ul>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * @SpringBootTest
 * class UserControllerIntegrationTest extends BaseIntegrationTest {
 *     // write tests here
 * }
 * }
 * </pre>
 */
@Testcontainers
@SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
public abstract class BaseIntegrationTest {
    private static String POSTGRESQL_SQL_IMAGE = "postgres:16-alpine";

    // The container is initialized only once and reused by every test suite inheriting from this class.
    // This dramatically reduces the performance cost of creating containers for each test.
    @ServiceConnection
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static
    {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRESQL_SQL_IMAGE));
        POSTGRES_CONTAINER.start();
    }
}
