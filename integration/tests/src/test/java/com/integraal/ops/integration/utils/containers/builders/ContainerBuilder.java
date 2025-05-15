package com.integraal.ops.integration.utils.containers.builders;

import com.integraal.ops.integration.utils.containers.beans.ContainersConfiguration;
import org.testcontainers.containers.Container;

public sealed interface ContainerBuilder permits
        PostgresContainerBuilder,
        WiremockContainerBuilder
{
    Container build(ContainersConfiguration configuration);
}
