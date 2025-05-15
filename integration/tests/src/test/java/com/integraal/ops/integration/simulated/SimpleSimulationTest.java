package com.integraal.ops.integration.simulated;

import com.integraal.ops.integration.utils.containers.beans.ContainerInstances;
import com.integraal.ops.integration.utils.orchestrations.TestOrchestrator;
import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class SimpleSimulationTest {

    @Test
    public void runSimpleTest() throws Exception {
        TestRunConfiguration testConfiguration = new TestRunConfiguration(
                new ContainerInstances(Optional.empty()),
                List.of(),
                List.of()
        );
        TestOrchestrator orchestrator = new TestOrchestrator();
        orchestrator.runTestLifeCycle(testConfiguration);
    }
}
