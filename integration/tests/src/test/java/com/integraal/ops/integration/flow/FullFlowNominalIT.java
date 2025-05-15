package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.IntegraalIntegrationTestApplication;
import com.integraal.ops.integration.flow.beans.EntrypointFlowInbean;
import com.integraal.ops.integration.utils.ApplicationComponents;
import com.integraal.ops.integration.utils.ContextUtils;
import com.integraal.ops.integration.utils.containers.constants.ContainerConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.MountableFile;
//import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = IntegraalIntegrationTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback(false)
public class FullFlowNominalIT {

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(ContainerConstants.POSTGRES_DOCKER_IMAGE_TAG)
        .withDatabaseName(ContainerConstants.POSTGRES_DATABASE_NAME)
        .withUsername(ContainerConstants.POSTGRES_USERNAME)
        .withPassword(ContainerConstants.POSTGRES_PASSWORD);

//    @Container
//    private static final WireMockContainer wiremockServerContainer = new WireMockContainer(TestConstants.WIREMOCK_DOCKER_IMAGE_TAG)
//        .withCopyToContainer(
//            MountableFile.forClasspathResource(
//                TestConstants.WIREMOCK_FRANCE_TRAVAIL_SCENARIO_DIRECTORY
//            ),
//            TestConstants.WIREMOCK_CONTAINER_PATH
//        );
    static {
        postgreSQLContainer.start();
//        wiremockServerContainer.withCliArg("--global-response-templating");
//        wiremockServerContainer.start();

//        var containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer, "");
//        ScriptUtils.runInitScript(containerDelegate, "init.sql");
//
//        wiremockServerContainer.waitingFor(Wait.forListeningPort());
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);

//        dynamicPropertyRegistry.add("provider.francetravail.endpoint.url", wiremockServerContainer::getBaseUrl);
    }

    @Autowired
    private ApplicationContext applicationContext;
    private ApplicationComponents components;

    @BeforeAll
    public void setupBeforeAll() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
         components = ContextUtils.setupAppComponents(applicationContext);
    }

        @AfterAll
    public static void cleanAfterAll() {
        postgreSQLContainer.stop();
//        wiremockServerContainer.stop();
    }

    @Test
    void franceTravailScenarioNominalFullTest() throws InterruptedException {
        EntrypointService franceTravailEntrypoint = components.getFlowConfigurationService().getEntrypointServiceForFlow(UUID.fromString("ce785cd9-fc26-4b99-9134-6275266d08ac"));
        Map<String, Object> dataInputEntryPoint = Map.of(
            "Name", "franceTravailScenarioNominalFullTest",
            "StartDate", OffsetDateTime.now(),
            "Purpose", "Test"
        );
        Map<String, String> dataMetadata = Map.of();
        OffsetDateTime receiveTime = OffsetDateTime.now();
        EntrypointFlowInbean entrypointFlowInbean = EntrypointFlowInbean.builder()
                .inputData(dataInputEntryPoint)
                .receivedDate(receiveTime)
                .inputMetaData(dataMetadata)
                .build();
        franceTravailEntrypoint.handleMessage(entrypointFlowInbean);

        Thread.sleep(5000);
    }

}
