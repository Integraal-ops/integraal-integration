package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.EntrypointFlowInbean;

public interface EntrypointService {

    public void handleMessage(EntrypointFlowInbean entrypointFlowInbean);

}
