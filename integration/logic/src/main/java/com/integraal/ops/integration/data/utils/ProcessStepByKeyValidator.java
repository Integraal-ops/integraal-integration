package com.integraal.ops.integration.data.utils;

import com.integraal.ops.integration.data.beans.ProcessStepRetrieveByKeyInBean;

public class ProcessStepByKeyValidator {
    public static boolean validateProcessStepByKeyInBean(ProcessStepRetrieveByKeyInBean processStepRetrieveByKeyInBean) {
        if (processStepRetrieveByKeyInBean == null) {
            return false;
        }
        return processStepRetrieveByKeyInBean.getFlowId().isPresent() ||
            processStepRetrieveByKeyInBean.getFlowKeyId().isPresent() ||
            processStepRetrieveByKeyInBean.getStepID().isPresent() ||
            processStepRetrieveByKeyInBean.getStepKeyId().isPresent();
    }
}
