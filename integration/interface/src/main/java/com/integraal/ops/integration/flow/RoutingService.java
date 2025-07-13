package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.RoutingInBean;
import com.integraal.ops.integration.transversal.services.LogicService;

public interface RoutingService  extends LogicService {

    void handleMessage(RoutingInBean routingInBean);

}
