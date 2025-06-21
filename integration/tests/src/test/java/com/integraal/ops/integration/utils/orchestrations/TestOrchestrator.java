package com.integraal.ops.integration.utils.orchestrations;

import com.integraal.ops.integration.flow.FlowConfigurationService;
import com.integraal.ops.integration.flow.FlowConfigurationServiceImpl;
import com.integraal.ops.integration.flow.beans.FlowStepInbean;
import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import com.integraal.ops.integration.utils.orchestrations.runners.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
            // Run actions
            // Sending Message to test behaviour
            FlowStepInbean flowStepInbean = FlowStepInbean.builder()
                    .flowId(Optional.of(UUID.fromString("ce785cd9-fc26-4b99-9134-6275266d08ac")))
                    .stepId(UUID.fromString("f057a8dd-d4e2-4231-8a64-526c75103d99"))
                    .flowKeyId(UUID.randomUUID())
                    .stepKeyId(UUID.randomUUID())
                    .flowDataId("dataId Flow")
                    .build();
            ((FlowConfigurationServiceImpl)
                    applicationsLaunched
                            .get("SimpleApplication-1")
                            .getBean(FlowConfigurationService.class)
            )
            .flowsToMessageChannels.get(Pair.of(UUID.fromString("ce785cd9-fc26-4b99-9134-6275266d08ac"), UUID.fromString("f057a8dd-d4e2-4231-8a64-526c75103d99")))
            .send(MessageBuilder.withPayload(flowStepInbean).build());

            // List flows
            System.out.println("==================== Print all flows ======================");
            IntegrationFlowContext integrationFlowContext = applicationsLaunched.get("SimpleApplication-1").getBean(IntegrationFlowContext.class);
            integrationFlowContext.getRegistry().keySet().forEach(System.out::println);

            Thread.sleep(1_500_000);
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
