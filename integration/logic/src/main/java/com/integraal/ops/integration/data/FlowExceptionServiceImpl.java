package com.integraal.ops.integration.data;

import com.integraal.ops.integration.model.persistence.jooq.generated.tables.pojos.FlowException;
import com.integraal.ops.integration.model.repositories.FlowExceptionRepository;
import com.integraal.ops.integration.data.beans.FlowExceptionStoreInBean;
import com.integraal.ops.integration.data.beans.FlowExceptionStoreOutBean;
import com.integraal.ops.integration.storage.errors.StorageWriteError;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
public class FlowExceptionServiceImpl implements FlowExceptionService {
    private final FlowExceptionRepository flowExceptionRepository;

    public FlowExceptionServiceImpl(FlowExceptionRepository flowExceptionRepository) {
        this.flowExceptionRepository = flowExceptionRepository;
    }

    @Override
    public Either<StorageWriteError, FlowExceptionStoreOutBean> storeExceptionData(FlowExceptionStoreInBean exceptionDataToStore) {
        Throwable exceptionToStore = exceptionDataToStore.getExceptionToStore();
        FlowException flowExceptions = new FlowException()
            .setExceptionType(exceptionToStore.getClass().getCanonicalName())
            .setMessage(exceptionToStore.getMessage())
            .setStackTrace(getStackTraceAsString(exceptionToStore))
            .setCreatedAt(OffsetDateTime.now())
            .setUpdatedAt(OffsetDateTime.now());
        try {
            flowExceptions = flowExceptionRepository.save(flowExceptions);
            FlowExceptionStoreOutBean flowExceptionStoreOutBean = FlowExceptionStoreOutBean.builder()
                .exceptionId(flowExceptions.getId())
                .build();
            return Either.right(flowExceptionStoreOutBean);
        } catch (Exception t) {
            return Either.left(new StorageWriteError.FatalSystemStorageWriteError(t));
        }
    }


    private static String getStackTraceAsString(Throwable exception) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
