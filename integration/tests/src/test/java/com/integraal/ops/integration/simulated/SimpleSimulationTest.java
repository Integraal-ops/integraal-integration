package com.integraal.ops.integration.simulated;

import com.integraal.ops.integration.utils.containers.ContainerLauncher;
import com.integraal.ops.integration.utils.containers.beans.ContainerInstances;
import com.integraal.ops.integration.utils.containers.beans.ContainersConfiguration;
import com.integraal.ops.integration.utils.containers.utils.DefaultConfigurationsFactory;
import com.integraal.ops.integration.utils.orchestrations.TestOrchestrator;
import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class SimpleSimulationTest {

    static ContainersConfiguration containersConfiguration = ContainersConfiguration.builder()
            .postgresConfiguration(Optional.of(DefaultConfigurationsFactory.defaultPostgresConfiguration()))
            .build();

    static ContainerInstances containerInstances = ContainerLauncher.createContainers(containersConfiguration);
    static {
        ContainerLauncher.startContainers(containerInstances);
    }

    @Test
    public void runSimpleTest() throws Exception {
        TestRunConfiguration testConfiguration = new TestRunConfiguration(
            containerInstances,
            List.of(),
            List.of()
        );
        TestOrchestrator orchestrator = new TestOrchestrator();
        orchestrator.runTestLifeCycle(testConfiguration);
    }
}
