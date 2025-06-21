package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.IssueHandlerInbean;
import com.integraal.ops.integration.model.persistence.FlowException;
import com.integraal.ops.integration.model.repositories.FlowExceptionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class IssueHandlerServiceImpl implements IssueHandlerService {
    private final FlowExceptionsRepository flowExceptionsRepository;

    @Autowired
    public IssueHandlerServiceImpl(FlowExceptionsRepository flowExceptionsRepository) {
        this.flowExceptionsRepository = flowExceptionsRepository;
    }

    @Override
    public void handleMessage(IssueHandlerInbean issueHandlerInbean) {
        log.info("Issue Handling flow steps in bean: '{}'", issueHandlerInbean);
        UUID exceptionId = issueHandlerInbean.getExceptionIdOnOriginStep();
        Optional<FlowException> exception = flowExceptionsRepository.findById(exceptionId);
        if (exception.isPresent()) {
            FlowException flowException = exception.get();
            log.error("Issue on handling flow step '{}': message : '{}' - '{}'", issueHandlerInbean.getFlowId(), flowException.getMessage(), flowException.getStackTrace());
        } else {
            log.error("Issue with no valid Exception ID '{}'", issueHandlerInbean.getFlowId());
        }
    }
}
