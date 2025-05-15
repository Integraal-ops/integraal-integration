package com.integraal.ops.integration.utils.containers.beans;

import com.integraal.ops.integration.utils.containers.beans.configuration.PostgresConfiguration;
import com.integraal.ops.integration.utils.containers.beans.configuration.WiremockConfiguration;

import java.util.Optional;

public record ContainersConfiguration(
    Optional<PostgresConfiguration> postgresConfiguration,
    Optional<WiremockConfiguration> wiremockConfiguration
) {
}
