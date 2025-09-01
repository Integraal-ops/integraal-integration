package com.integraal.ops.integration.model.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource(value = "properties/database-queries.properties")
})
public class PropertiesConfiguration {
}
