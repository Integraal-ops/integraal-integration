package com.integraal.ops.integration.utils.orchestrations.runners;

import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;

public sealed interface TestStepRunner
        permits RunnerActions, RunnerApplicationLauncher, RunnerAssertions, RunnerCleanup, RunnerInitializationData, RunnerMock
{
    void run(TestRunConfiguration runConfiguration) throws Exception;
}
