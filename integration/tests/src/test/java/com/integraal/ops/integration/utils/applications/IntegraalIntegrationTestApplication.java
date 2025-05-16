package com.integraal.ops.integration.utils.applications;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {
        "com.integraal.ops.integration.utils.applications",
        "com.integraal.ops.integration.transversal",
        "com.integraal.ops.integration.model",
        "com.integraal.ops.integration.flow",
    }
)
@EntityScan("com.integraal.ops.integration.model")
@EnableJpaRepositories("com.integraal.ops.integration.model")
public class IntegraalIntegrationTestApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(IntegraalIntegrationTestApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
