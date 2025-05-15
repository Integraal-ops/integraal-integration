package com.integraal.ops.integration.utils.containers;

import com.integraal.ops.integration.utils.containers.beans.ContainerInstances;
import com.integraal.ops.integration.utils.containers.beans.ContainersConfiguration;
import com.integraal.ops.integration.utils.containers.builders.PostgresContainerBuilder;

public class ContainerLauncher {

    public static ContainerInstances createContainers(ContainersConfiguration containersConfiguration) {
        return new ContainerInstances(
            containersConfiguration.getPostgresConfiguration().map(PostgresContainerBuilder::build)
        );
    }

    public static void startContainers(ContainerInstances containerInstances) {
        containerInstances.postgresContainer().ifPresent(PostgresContainerBuilder::startPostgresContainerWithInitialization);
    }

}
