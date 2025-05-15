package com.integraal.ops.integration.utils.orchestrations;

import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import com.integraal.ops.integration.utils.orchestrations.runners.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestOrchestrator {

    public void runTestLifeCycle(TestRunConfiguration testConfiguration) throws Exception {
        log.info("Running test for configuration '{}'", testConfiguration);
        // Cleanup containers
        RunnerCleanup cleanup = new RunnerCleanup();
        cleanup.run(testConfiguration);
        try {
            // Setup Initial data
            RunnerInitializationData initializationData  = new RunnerInitializationData();
            initializationData.run(testConfiguration);
            // Setup Mocks (Wiremock / Api mocks ...)
            RunnerMock mockRunner = new RunnerMock();
            mockRunner.run(testConfiguration);
            // Launch the applications
            RunnerApplicationLauncher launcher = new RunnerApplicationLauncher();
            launcher.run(testConfiguration);
            // Run all manual actions
            RunnerActions runnerActions = new RunnerActions();
            runnerActions.run(testConfiguration);
            // Run assertions
            RunnerAssertions assertionsRunner = new RunnerAssertions();
            assertionsRunner.run(testConfiguration);
        } catch (Exception e) {
            log.error("Error when orchestrating test '{}'", testConfiguration, e);
        } finally {
            // shutdown
            RunnerApplicationLauncher shutdown = new RunnerApplicationLauncher();
            shutdown.shutdownApplication(testConfiguration);
            // Cleanup containers
            cleanup.run(testConfiguration);
        }
    }
}
