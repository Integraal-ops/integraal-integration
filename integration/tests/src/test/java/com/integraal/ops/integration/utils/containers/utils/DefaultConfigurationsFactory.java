package com.integraal.ops.integration.utils.containers.utils;

import com.integraal.ops.integration.utils.containers.beans.configuration.PostgresConfiguration;

import static com.integraal.ops.integration.utils.containers.constants.ContainerConstants.POSTGRES_DATABASE_NAME;
import static com.integraal.ops.integration.utils.containers.constants.ContainerConstants.POSTGRES_DOCKER_IMAGE_TAG;
import static com.integraal.ops.integration.utils.containers.constants.ContainerConstants.POSTGRES_PASSWORD;
import static com.integraal.ops.integration.utils.containers.constants.ContainerConstants.POSTGRES_USERNAME;

public final class DefaultConfigurationsFactory {
    public static PostgresConfiguration defaultPostgresConfiguration() {
        return PostgresConfiguration.builder()
            .username(POSTGRES_USERNAME)
            .password(POSTGRES_PASSWORD)
            .dbName(POSTGRES_DATABASE_NAME)
            .postgresContainerVersion(POSTGRES_DOCKER_IMAGE_TAG)
            .initScriptPath("database/integration-init.sql")
            .cleanupScriptPath("database/integration-cleanup.sql")
            .build();
    }
}
