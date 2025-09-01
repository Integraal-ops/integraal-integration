package com.integraal.ops.integration.model.repositories;

import com.integraal.ops.integration.model.configuration.DefaultQueriesConfigurationService;
import com.integraal.ops.integration.model.configuration.UuidGenerator;
import com.integraal.ops.integration.model.persistence.jooq.generated.tables.pojos.FlowException;
import com.integraal.ops.integration.model.persistence.jooq.generated.tables.records.FlowExceptionRecord;
import jakarta.transaction.Transactional;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.integraal.ops.integration.model.persistence.jooq.generated.Tables.FLOW_EXCEPTION;
import static com.integraal.ops.integration.model.persistence.jooq.generated.Tables.PROCESS_STEP;

@Repository
public class FlowExceptionRepositoryImpl implements FlowExceptionRepository {
    private static final OrderField<?> DEFAULT_ORDER_FIELD = FLOW_EXCEPTION.CREATED_AT.desc();

    private final DSLContext dslContext;
    private final DefaultQueriesConfigurationService  defaultQueriesConfigurationService;
    private final UuidGenerator uuidGenerator;

    @Autowired
    public FlowExceptionRepositoryImpl(
        DataSource dataSource,
        DefaultQueriesConfigurationService defaultQueriesConfigurationService,
        UuidGenerator uuidGenerator
    ) {
        this.dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
        this.defaultQueriesConfigurationService = defaultQueriesConfigurationService;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public Optional<FlowException> findById(UUID oid) {
        return dslContext.selectFrom(FLOW_EXCEPTION)
            .where(FLOW_EXCEPTION.ID.eq(oid))
            .fetchOptionalInto(FlowException.class);
    }

    @Override
    public Page<FlowException> findAllByPredicate(
        Condition condition,
        OrderField<?> orderBy,
        Pageable pageable
    ) {
        Long totalCount = dslContext.selectCount().from(FLOW_EXCEPTION).where(condition).fetchOne(0, Long.class);
        List<FlowException> exceptions = dslContext.selectFrom(FLOW_EXCEPTION)
            .where(condition)
            .orderBy(orderBy)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchInto(FlowException.class);
        return new PageImpl<>(exceptions, pageable, totalCount);
    }

    @Override
    public Page<FlowException> findAllByPredicate(Condition condition, Pageable pageable) {
        return findAllByPredicate(condition, DEFAULT_ORDER_FIELD, pageable);
    }

    @Override
    public Page<FlowException> findAllByPredicate(Condition condition) {
        Integer defaultPageSize = this.defaultQueriesConfigurationService.getDefaultNumericalProperty(DefaultQueriesConfigurationService.NumericalProperties.DEFAULT_PAGE_SIZE);
        Pageable pageable = PageRequest.of(0, defaultPageSize);
        return findAllByPredicate(condition, DEFAULT_ORDER_FIELD, pageable);
    }

    @Override
    @Transactional
    public FlowException save(FlowException flowException) {
        FlowExceptionRecord record = dslContext.newRecord(FLOW_EXCEPTION, flowException);
        UUID generatedId = uuidGenerator.generateUuidForDB(record);
        record.setId(generatedId);
        return dslContext.insertInto(FLOW_EXCEPTION).set(record)
            .returning().fetchOneInto(FlowException.class);
    }
}
