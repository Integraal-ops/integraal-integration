package com.integraal.ops.integration.utils.containers.beans;

import com.integraal.ops.integration.utils.containers.beans.wrappers.PostgresContainerWrapper;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

public record ContainerInstances(
    Optional<PostgresContainerWrapper> postgresContainer
//    Optional<WireMockContainer> wiremockContainer
) {
}
