package com.integraal.ops.integration.data;

import com.integraal.ops.integration.data.beans.ProcessStepRetrieveByKeyInBean;
import com.integraal.ops.integration.data.beans.ProcessStepRetrieveByKeyOutBean;
import com.integraal.ops.integration.data.errors.ReadDatabaseError;
import com.integraal.ops.integration.data.predicates.ProcessStepPredicate;
import com.integraal.ops.integration.data.utils.ProcessStepByKeyValidator;
import com.integraal.ops.integration.model.persistence.jooq.generated.tables.pojos.ProcessStep;
import com.integraal.ops.integration.model.repositories.ProcessStepRepository;
import com.integraal.ops.integration.transversal.exceptions.ServiceFatalException;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProcessStepServiceImpl implements ProcessStepService {
    private final ProcessStepRepository processStepRepository;


    public ProcessStepServiceImpl(
        ProcessStepRepository processStepRepository
    ) {
        this.processStepRepository = processStepRepository;
    }

    @Override
    public Either<ReadDatabaseError, ProcessStepRetrieveByKeyOutBean> retrieveByKey(ProcessStepRetrieveByKeyInBean processStepRetrieveByKeyInBean) {
        try {
            boolean isValidInput = ProcessStepByKeyValidator.validateProcessStepByKeyInBean(processStepRetrieveByKeyInBean);
            if (!isValidInput) {
                String errorMessage = String.format("['%s']['retrieveByKey'] invalid processStepRetrieveByKeyInBean for query. Here was 'processStepRetrieveByKeyInBean' '%s'", ProcessStepServiceImpl.class.getName(), processStepRetrieveByKeyInBean);
                return Either.left(new ReadDatabaseError.FunctionalReadDatabaseError(
                    errorMessage,
                    new ServiceFatalException(errorMessage))
                );
            }
            List<Condition> queryConditions = new ArrayList<>();
            processStepRetrieveByKeyInBean.getFlowId().map(ProcessStepPredicate::processWithFlowId).ifPresent(queryConditions::add);
            processStepRetrieveByKeyInBean.getFlowKeyId().map(ProcessStepPredicate::processWithFlowKeyId).ifPresent(queryConditions::add);
            processStepRetrieveByKeyInBean.getStepID().map(ProcessStepPredicate::processWithStepId).ifPresent(queryConditions::add);
            processStepRetrieveByKeyInBean.getStepKeyId().map(ProcessStepPredicate::processWithStepKeyId).ifPresent(queryConditions::add);
            Condition searchCondition = DSL.and(queryConditions.toArray(new Condition[0]));
            Page<ProcessStep> processList = processStepRepository.findAllByPredicate(searchCondition);
            if (processList.getTotalElements() == 0) {
                String errorMessage = String.format("['%s']['retrieveByKey'] No ProcessStep Found In database. Here was 'processStepRetrieveByKeyInBean' '%s'", ProcessStepServiceImpl.class.getName(), processStepRetrieveByKeyInBean);
                return Either.left(new ReadDatabaseError.FunctionalReadDatabaseError(
                    errorMessage,
                    new ServiceFatalException(errorMessage))
                );
            }
            if (processList.getTotalElements() != 1) {
                String errorMessage = String.format("['%s']['retrieveByKey'] Found multiple Process for a Key Query. Here was 'processStepRetrieveByKeyInBean' '%s'", ProcessStepServiceImpl.class.getName(), processStepRetrieveByKeyInBean);
                return Either.left(new ReadDatabaseError.FunctionalReadDatabaseError(
                    errorMessage,
                    new ServiceFatalException(errorMessage))
                );
            }
            ProcessStepRetrieveByKeyOutBean result = ProcessStepRetrieveByKeyOutBean.builder()
                .processStep(processList.getContent().get(0))
                .build();
            return Either.right(result);
        } catch (Exception e) {
            return Either.left(new ReadDatabaseError.TechnicalReadDatabaseError(
                String.format("['%s']['retrieveByKey'] Technical Error occurred with DB Query . Here was 'processStepRetrieveByKeyInBean' '%s'", ProcessStepServiceImpl.class.getName(), processStepRetrieveByKeyInBean),
                e
            ));
        }
    }
}
