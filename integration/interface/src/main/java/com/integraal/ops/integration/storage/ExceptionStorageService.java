package com.integraal.ops.integration.storage;

import com.integraal.ops.integration.storage.beans.ExceptionStoreInBean;
import com.integraal.ops.integration.storage.beans.ExceptionStoreOutBean;
import com.integraal.ops.integration.storage.errors.StorageWriteError;
import com.integraal.ops.integration.transversal.services.LogicService;
import io.vavr.control.Either;

public interface ExceptionStorageService extends LogicService {
    Either<StorageWriteError, ExceptionStoreOutBean> storeExceptionData(ExceptionStoreInBean userDataToStore);
}
