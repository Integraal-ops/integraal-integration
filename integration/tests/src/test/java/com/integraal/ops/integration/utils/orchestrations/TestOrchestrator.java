package com.integraal.ops.integration.utils.orchestrations;

import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import com.integraal.ops.integration.utils.orchestrations.runners.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestOrchestrator {

    public void runTestLifeCycle(TestRunConfiguration testConfiguration) throws Exception {
        log.info("Running test for configuration '{}'", testConfiguration.testName());
        // Cleanup containers
        RunnerCleanup cleanup = new RunnerCleanup();
        cleanup.run(testConfiguration);
        Map<String, ConfigurableApplicationContext> applicationsLaunched = new HashMap<>();
        try {
            // Setup Initial data
            RunnerInitializationData initializationData  = new RunnerInitializationData();
            initializationData.run(testConfiguration);
            // Setup Mocks (Wiremock / Api mocks ...)
            RunnerMock mockRunner = new RunnerMock();
            mockRunner.run(testConfiguration);
            // Launch the applications
            applicationsLaunched = RunnerApplicationLauncher.run(testConfiguration);
            // Run all manual actions
            RunnerActions runnerActions = new RunnerActions();
            runnerActions.run(testConfiguration);
            // Run assertions
            RunnerAssertions assertionsRunner = new RunnerAssertions();
            assertionsRunner.run(testConfiguration);
        } catch (Exception e) {
            log.error("Error when orchestrating test '{}'", testConfiguration.testName(), e);
            throw e;
        } finally {
            // shutdown
            RunnerApplicationLauncher.shutdownApplication(applicationsLaunched);
            // Cleanup containers
            cleanup.run(testConfiguration);
        }
    }
}
