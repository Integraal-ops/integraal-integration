package com.integraal.ops.integration.model.repositories;

import com.integraal.ops.integration.model.configuration.DefaultQueriesConfigurationService;
import com.integraal.ops.integration.model.configuration.UuidGenerator;
import com.integraal.ops.integration.model.persistence.jooq.generated.tables.pojos.ProcessStep;
import com.integraal.ops.integration.model.persistence.jooq.generated.tables.records.ProcessStepRecord;
import jakarta.transaction.Transactional;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.integraal.ops.integration.model.persistence.jooq.generated.Tables.PROCESS_STEP;

@Repository
public class ProcessStepRepositoryImpl implements ProcessStepRepository {
    private static final OrderField<?> DEFAULT_ORDER_FIELD = PROCESS_STEP.CREATED_AT.desc();

    private final DSLContext dslContext;
    private final DefaultQueriesConfigurationService defaultQueriesConfigurationService;
    private final UuidGenerator uuidGenerator;

    public ProcessStepRepositoryImpl(
        DataSource dataSource,
        DefaultQueriesConfigurationService defaultQueriesConfigurationService,
        UuidGenerator uuidGenerator
    ) {
        this.dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
        this.defaultQueriesConfigurationService = defaultQueriesConfigurationService;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public Optional<ProcessStep> findById(UUID oid) {
        return dslContext.selectFrom(PROCESS_STEP)
            .where(PROCESS_STEP.ID.eq(oid))
            .fetchOptionalInto(ProcessStep.class);
    }

    @Override
    public Page<ProcessStep> findAllByPredicate(Condition condition, OrderField<?> orderBy, Pageable pageable) {
        Long totalCount = dslContext.selectCount().from(PROCESS_STEP).where(condition).fetchOne(0, Long.class);
        List<ProcessStep> processStepList = dslContext.selectFrom(PROCESS_STEP)
            .where(condition)
            .orderBy(orderBy)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchInto(ProcessStep.class);
        return new PageImpl<>(processStepList, pageable, totalCount);
    }

    @Override
    public Page<ProcessStep> findAllByPredicate(Condition condition, Pageable pageable) {
        return findAllByPredicate(condition, DEFAULT_ORDER_FIELD, pageable);
    }

    @Override
    public Page<ProcessStep> findAllByPredicate(Condition condition) {
        Integer defaultPageSize = this.defaultQueriesConfigurationService.getDefaultNumericalProperty(DefaultQueriesConfigurationService.NumericalProperties.DEFAULT_PAGE_SIZE);
        Pageable pageable = PageRequest.of(0, defaultPageSize);
        return findAllByPredicate(condition, DEFAULT_ORDER_FIELD, pageable);
    }

    @Override
    @Transactional
    public ProcessStep save(ProcessStep processStep) {
        ProcessStepRecord record = dslContext.newRecord(PROCESS_STEP, processStep);
        UUID generatedId = uuidGenerator.generateUuidForDB(record);
        record.setId(generatedId);
        return dslContext.insertInto(PROCESS_STEP).set(record)
            .returning().fetchOneInto(ProcessStep.class);
    }
}
