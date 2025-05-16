package com.integraal.ops.integration.utils.orchestrations.beans;

import com.integraal.ops.integration.utils.containers.beans.ContainerInstances;
import com.integraal.ops.integration.utils.orchestrations.beans.actions.ActionConfiguration;
import com.integraal.ops.integration.utils.orchestrations.beans.application.ApplicationToRun;
import com.integraal.ops.integration.utils.orchestrations.beans.mocks.MockConfiguration;

import java.util.List;

public record TestRunConfiguration(
    String testName,
    ContainerInstances containerInstances,
    List<MockConfiguration> mockConfigurationList,
    List<ApplicationToRun> applicationToRunList,
    List<ActionConfiguration> manualActions
) {
}
