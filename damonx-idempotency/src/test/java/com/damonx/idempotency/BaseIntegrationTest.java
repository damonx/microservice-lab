package com.damonx.idempotency;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
public abstract class BaseIntegrationTest {
    private static String POSTGRESQL_SQL_IMAGE = "postgres:16-alpine";

    // As this is statically initialised, the same container is used by all the test suites that extend
    // this class; that way, we don't pay a heavy price for starting up a container...
    @ServiceConnection
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static
    {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRESQL_SQL_IMAGE));
        POSTGRES_CONTAINER.start();
    }
}
