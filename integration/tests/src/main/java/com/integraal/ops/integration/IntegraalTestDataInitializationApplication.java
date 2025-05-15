package com.integraal.ops.integration;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(
    scanBasePackages={
        "com.integraal.ops.integration.transversal",
        "com.integraal.ops.integration.model",
    })
public class IntegraalTestDataInitializationApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(IntegraalTestDataInitializationApplication.class)
            .web(WebApplicationType.NONE)
            .run(args);
    }
}
