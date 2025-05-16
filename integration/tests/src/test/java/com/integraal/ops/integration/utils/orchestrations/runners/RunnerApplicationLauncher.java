package com.integraal.ops.integration.utils.orchestrations.runners;

import com.integraal.ops.integration.utils.containers.beans.ContainerInstances;
import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import com.integraal.ops.integration.utils.orchestrations.beans.application.ApplicationToRun;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class RunnerApplicationLauncher {

    public static Map<String, ConfigurableApplicationContext> run(TestRunConfiguration runConfiguration) throws Exception {
        Map<String, ConfigurableApplicationContext> contexts = new HashMap<>();
        for (ApplicationToRun applicationToRun : runConfiguration.applicationToRunList()) {
            log.info("Launching application '{}' with class '{}'",
                applicationToRun.getApplicationName(),
                applicationToRun.getApplicationClass().getCanonicalName()
            );
            Map<String, Object> standardCommonProperties = buildApplicationProperties(runConfiguration.containerInstances(), applicationToRun);
            ConfigurableApplicationContext applicationContext = buildAndStartApplication(applicationToRun, standardCommonProperties);
            contexts.put(applicationToRun.getApplicationName(), applicationContext);
        }
        return contexts;
    }

    public static void shutdownApplication(Map<String, ConfigurableApplicationContext> applicationStartedList) throws Exception {
        log.info("Shutting down application - Not implemented yet");
    }

    private static Map<String, Object> buildApplicationProperties(ContainerInstances containerInstances, ApplicationToRun applicationToRun) {
        Map<String, Object> standardCommonProperties = new HashMap<>();
        containerInstances.postgresContainer().ifPresent(container -> {
            appendPostgresContainerProperties(container.container(), standardCommonProperties);
        });
        standardCommonProperties.putAll(applicationToRun.getApplicationProperties());
        return standardCommonProperties;
    }

    private static Map<String, Object> appendPostgresContainerProperties(PostgreSQLContainer container, Map<String, Object> properties) {
        properties.put("spring.datasource.url", container.getJdbcUrl());
        properties.put("spring.datasource.username", container.getUsername());
        properties.put("spring.datasource.password", container.getPassword());
        return properties;
    }

    private static ConfigurableApplicationContext buildAndStartApplication(ApplicationToRun applicationToRun, Map<String, Object> properties) throws InterruptedException {
        Class<?>[] aopApplications = applicationToRun.getAopClasses().toArray(new Class<?>[0]);
        SpringApplicationBuilder appBuilder = new SpringApplicationBuilder(applicationToRun.getApplicationClass())
            .web(applicationToRun.getApplicationType())
            .bannerMode(Banner.Mode.OFF)
            .sources(aopApplications);
        SpringApplication app = appBuilder.build();
        app.addInitializers(
                context -> context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("test-properties", properties))
        );
        ConfigurableApplicationContext applicationContext = app.run();
        while (!applicationContext.isActive()) {
            Thread.sleep(200);
        }
        return applicationContext;
    }

}
