package com.integraal.ops.integration.flow.errors;

import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;

public sealed interface FlowRoutingError
    permits FlowRoutingError.UnknownFlowStepRouting
{

    Exception toException();

    record UnknownFlowStepRouting(
        ServiceFatalException flowException
    ) implements FlowRoutingError {
        @Override
        public Exception toException() {
            return flowException;
        }
    }
}
