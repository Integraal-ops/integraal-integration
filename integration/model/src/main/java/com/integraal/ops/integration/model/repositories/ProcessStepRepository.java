package com.integraal.ops.integration.model.repositories;

import com.integraal.ops.integration.model.persistence.jooq.generated.tables.pojos.ProcessStep;
import org.jooq.Condition;
import org.jooq.OrderField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessStepRepository {
    Optional<ProcessStep> findById(UUID oid);
    Page<ProcessStep> findAllByPredicate(Condition condition, OrderField<?> orderBy, Pageable pageable);
    Page<ProcessStep> findAllByPredicate(Condition condition, Pageable pageable);
    Page<ProcessStep> findAllByPredicate(Condition condition);

    ProcessStep save(ProcessStep processStep);
}
