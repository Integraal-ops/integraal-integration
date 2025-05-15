package com.integraal.ops.integration.utils.orchestrations.runners;

import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RunnerApplicationLauncher implements TestStepRunner {
    // Add a registry of application launched for lifecycle

    @Override
    public void run(TestRunConfiguration runConfiguration) throws Exception {
        log.info("Running application launcher - Not implemented yet");
    }

    public void shutdownApplication(TestRunConfiguration runConfiguration) throws Exception {
        log.info("Shutting down application - Not implemented yet");
    }
}
