package com.integraal.ops.integration.flow;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogProcessorService implements ProcessorService {
    @Override
    public Object processFlowMessage(Object data) throws Exception {
        return null;
    }

    @Override
    public String registerNewData(Object newData, Object oldData, String oldDataId) throws Exception {
        return "";
    }

    @Override
    public UUID getStepUUID() {
        return null;
    }

    @Override
    public Object getDataFromId(String dataId) throws Exception {
        return null;
    }
}
