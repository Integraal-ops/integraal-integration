package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.EntrypointFlowInbean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EntrypointServiceExemple1Impl implements EntrypointService {
    @Override
    public void handleMessage(EntrypointFlowInbean entrypointFlowInbean) {
        log.info("EntrypointServiceExemple1Impl '{}'", entrypointFlowInbean);
    }
}
