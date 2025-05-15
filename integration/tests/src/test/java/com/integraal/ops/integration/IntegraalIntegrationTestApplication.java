package com.integraal.ops.integration;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication(
        scanBasePackages={
            "com.integraal.ops.integration.transversal",
            "com.integraal.ops.integration.model",
            "com.integraal.ops.integration.flow",
        })
public class IntegraalIntegrationTestApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(IntegraalIntegrationTestApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
