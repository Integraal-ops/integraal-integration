package com.integraal.ops.integration.utils.containers.builders;

import com.integraal.ops.integration.utils.containers.beans.ContainersConfiguration;
import com.integraal.ops.integration.utils.containers.beans.configuration.WiremockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.Container;

@Slf4j
public final class WiremockContainerBuilder {
    public static Container build(WiremockConfiguration configuration) {
        log.info("Building Wiremock container - Not Implemented yet");
        return null;
    }
}
