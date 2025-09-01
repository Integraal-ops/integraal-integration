package com.integraal.ops.integration.model.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultQueriesConfigurationServiceImpl implements DefaultQueriesConfigurationService {
    private final Integer defaultPageSize;

    @Autowired
    public DefaultQueriesConfigurationServiceImpl(
        @Value("${database.queries.default.page.size}") Integer defaultPageSize
    ) {
        this.defaultPageSize = defaultPageSize;
    }


    @Override
    public Integer getDefaultNumericalProperty(NumericalProperties numericalProperties) {
        return switch (numericalProperties) {
            case DEFAULT_PAGE_SIZE -> this.defaultPageSize;
        };
    }
}
