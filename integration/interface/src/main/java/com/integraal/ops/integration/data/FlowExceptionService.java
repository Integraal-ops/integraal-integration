package com.integraal.ops.integration.data;

import com.integraal.ops.integration.data.beans.FlowExceptionStoreInBean;
import com.integraal.ops.integration.data.beans.FlowExceptionStoreOutBean;
import com.integraal.ops.integration.storage.errors.StorageWriteError;
import com.integraal.ops.integration.transversal.services.LogicService;
import io.vavr.control.Either;

public interface FlowExceptionService extends LogicService {
    Either<StorageWriteError, FlowExceptionStoreOutBean> storeExceptionData(FlowExceptionStoreInBean userDataToStore);
}
