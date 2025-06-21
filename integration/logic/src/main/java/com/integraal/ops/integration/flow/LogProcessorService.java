package com.integraal.ops.integration.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class LogProcessorService implements ProcessorService {
    private final MessageChannel routingChannel;

    public LogProcessorService(MessageChannel routingChannel) {
        this.routingChannel = routingChannel;
    }

    @Override
    public MessageChannel getRoutingChannel() {
        return routingChannel;
    }

    @Override
    public Object processFlowMessage(Object data) throws Exception {
        log.info("Processing flow message with data: '{}'", data);
        return data;
    }

    @Override
    public String registerNewData(Object newData, Object oldData, String oldDataId) throws Exception {
        log.info("Registering new data: '{}'", newData);
        return "";
    }

    @Override
    public UUID getStepUUID() {
        log.info("Get step UUID");
        return null;
    }

    @Override
    public Object getDataFromId(String dataId) throws Exception {
        log.info("Get data from id '{}'", dataId);
        return null;
    }
}
