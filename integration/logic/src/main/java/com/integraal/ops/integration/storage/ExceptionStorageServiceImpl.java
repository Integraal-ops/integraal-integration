package com.integraal.ops.integration.storage;

import com.integraal.ops.integration.model.persistence.FlowException;
import com.integraal.ops.integration.model.repositories.FlowExceptionsRepository;
import com.integraal.ops.integration.storage.beans.ExceptionStoreInBean;
import com.integraal.ops.integration.storage.beans.ExceptionStoreOutBean;
import com.integraal.ops.integration.storage.errors.StorageWriteError;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
public class ExceptionStorageServiceImpl implements ExceptionStorageService {
    private final FlowExceptionsRepository flowExceptionsRepository;

    public ExceptionStorageServiceImpl(FlowExceptionsRepository flowExceptionsRepository) {
        this.flowExceptionsRepository = flowExceptionsRepository;
    }

    @Override
    public Either<StorageWriteError, ExceptionStoreOutBean> storeExceptionData(ExceptionStoreInBean exceptionDataToStore) {
        Throwable exceptionToStore = exceptionDataToStore.getExceptionToStore();
        FlowException flowExceptions = FlowException.builder()
            .exceptionType(exceptionToStore.getClass().getCanonicalName())
            .message(exceptionToStore.getMessage())
            .stackTrace(getStackTraceAsString(exceptionToStore))
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        try {
            flowExceptions = flowExceptionsRepository.save(flowExceptions);
            ExceptionStoreOutBean exceptionStoreOutBean = ExceptionStoreOutBean.builder()
                .exceptionId(flowExceptions.getId())
                .build();
            return Either.right(exceptionStoreOutBean);
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
