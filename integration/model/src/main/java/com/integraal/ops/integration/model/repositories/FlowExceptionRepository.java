package com.integraal.ops.integration.model.repositories;

import com.integraal.ops.integration.model.persistence.jooq.generated.tables.pojos.FlowException;
import org.jooq.Condition;
import org.jooq.OrderField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlowExceptionRepository {
    Optional<FlowException> findById(UUID oid);
    Page<FlowException> findAllByPredicate(Condition condition, OrderField<?> orderBy, Pageable pageable);
    Page<FlowException> findAllByPredicate(Condition condition, Pageable pageable);
    Page<FlowException> findAllByPredicate(Condition condition);

    FlowException save(FlowException flowException);
}
