package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.RoutingInBean;

public interface RoutingService {

    public void handleMessage(RoutingInBean routingInBean);

}
