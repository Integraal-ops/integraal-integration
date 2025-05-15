package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.EndpointFlowInbean;
import com.integraal.ops.integration.transversal.services.LogicService;

public interface EndpointService extends LogicService {

    public void handleMessage(EndpointFlowInbean endpointFlowInbean);

}
