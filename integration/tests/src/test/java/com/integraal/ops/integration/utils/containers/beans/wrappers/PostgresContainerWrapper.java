package com.integraal.ops.integration.utils.containers.beans.wrappers;

import org.testcontainers.containers.PostgreSQLContainer;

public record PostgresContainerWrapper(
    PostgreSQLContainer container,
    String initScript,
    String cleanupScript
) {
}
