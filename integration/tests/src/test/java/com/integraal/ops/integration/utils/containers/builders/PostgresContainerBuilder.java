package com.integraal.ops.integration.utils.containers.builders;

import com.integraal.ops.integration.utils.containers.beans.ContainersConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.Container;

@Slf4j
public final class PostgresContainerBuilder implements ContainerBuilder {
    @Override
    public Container build(ContainersConfiguration configuration) {
        log.info("Building Postgres container - Not Implemented yet");
        return null;
    }
}
