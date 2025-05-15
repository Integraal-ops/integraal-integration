package com.integraal.ops.integration.utils.orchestrations.beans;

import com.integraal.ops.integration.utils.containers.beans.ContainerInstances;
import com.integraal.ops.integration.utils.orchestrations.beans.actions.ActionConfiguration;
import com.integraal.ops.integration.utils.orchestrations.beans.mocks.MockConfiguration;

import java.util.List;

public record TestRunConfiguration(
    ContainerInstances containerInstances,
    List<ActionConfiguration> manualActions,
    List<MockConfiguration> mockConfigurationList
) {
}
