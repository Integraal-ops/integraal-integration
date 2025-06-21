package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.EndpointFlowInbean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EndpointServiceExemple1Impl implements EndpointService {
    @Override
    public void handleMessage(EndpointFlowInbean endpointFlowInbean) {
        log.info("EndpointServiceExemple1Impl.handleMessage '{}'", endpointFlowInbean);
    }
}
