package com.integraal.ops.integration.utils.orchestrations.runners;

import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import com.integraal.ops.integration.utils.orchestrations.beans.mocks.MockConfiguration;
import com.integraal.ops.integration.utils.orchestrations.beans.mocks.MockWireMockConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public sealed class RunnerMock implements TestStepRunner
        permits RunnerMockWiremock
{
    @Override
    public void run(TestRunConfiguration runConfiguration) throws Exception {
        log.info("==== Running RunnerMock ====");
        for (MockConfiguration mock : runConfiguration.mockConfigurationList()) {
            switch (mock) {
                case MockWireMockConfiguration mockWireMockConfiguration ->
                    new RunnerMockWiremock().applyMock(mockWireMockConfiguration);
            }
        }
    }


}
