package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.EntrypointFlowInbean;
import com.integraal.ops.integration.transversal.services.LogicService;

public interface EntrypointService extends LogicService {
    void handleMessage(EntrypointFlowInbean entrypointFlowInbean);
}
