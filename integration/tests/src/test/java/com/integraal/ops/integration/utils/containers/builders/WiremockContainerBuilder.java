package com.integraal.ops.integration.utils.containers.builders;

import com.integraal.ops.integration.utils.containers.beans.ContainersConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.Container;

@Slf4j
public final class WiremockContainerBuilder implements ContainerBuilder{
    @Override
    public Container build(ContainersConfiguration configuration) {
        log.info("Building Wiremock container - Not Implemented yet");
        return null;
    }
}
