package com.integraal.ops.integration;

import com.integraal.ops.integration.flow.EntrypointService;
import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;

import java.util.UUID;

public interface FlowConfigurationService {

    EntrypointService getEntrypointServiceForFlow(UUID flowId) throws ServiceFatalException;
}
