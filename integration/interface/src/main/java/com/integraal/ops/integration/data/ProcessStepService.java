package com.integraal.ops.integration.data;

import com.integraal.ops.integration.data.beans.ProcessStepRetrieveByKeyInBean;
import com.integraal.ops.integration.data.beans.ProcessStepRetrieveByKeyOutBean;
import com.integraal.ops.integration.data.errors.ReadDatabaseError;
import io.vavr.control.Either;

public interface ProcessStepService {
    Either<ReadDatabaseError, ProcessStepRetrieveByKeyOutBean> retrieveByKey(ProcessStepRetrieveByKeyInBean processStepRetrieveByKeyInBean);
}
