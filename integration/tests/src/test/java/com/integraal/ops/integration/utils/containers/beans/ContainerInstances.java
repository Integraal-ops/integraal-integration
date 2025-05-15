package com.integraal.ops.integration.utils.containers.beans;

import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

public record ContainerInstances(
    Optional<PostgreSQLContainer> postgresContainer
//    Optional<WireMockContainer> wiremockContainer
) {
}
