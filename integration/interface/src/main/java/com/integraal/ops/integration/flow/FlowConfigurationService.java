package com.integraal.ops.integration.flow;

import ch.qos.logback.core.joran.sanity.Pair;
import com.integraal.ops.integration.flow.errors.FlowRoutingError;
import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import com.integraal.ops.integration.transversal.services.LogicService;
import io.vavr.control.Either;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface FlowConfigurationService extends LogicService {


    EntrypointService getEntrypointServiceForFlow(UUID flowId) throws ServiceFatalException;

    Optional<MessageChannel> getMessageChannel(UUID flowId, UUID stepId);

    Either<FlowRoutingError, List<UUID>> getNextStepsInFlow(UUID flowKeyId, UUID stepKeyId);

    Either<FlowRoutingError, UUID> getInitialStepInFlow(UUID flowKeyId);

    Either<FlowRoutingError, MessageChannel> getMessageChannelForStepId(UUID stepKeyId);
}
