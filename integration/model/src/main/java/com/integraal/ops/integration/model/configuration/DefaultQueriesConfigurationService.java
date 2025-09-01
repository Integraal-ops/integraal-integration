package com.integraal.ops.integration.model.configuration;

public interface DefaultQueriesConfigurationService {

    enum NumericalProperties {
        DEFAULT_PAGE_SIZE,
    }
    Integer getDefaultNumericalProperty(NumericalProperties numericalProperties);
}
