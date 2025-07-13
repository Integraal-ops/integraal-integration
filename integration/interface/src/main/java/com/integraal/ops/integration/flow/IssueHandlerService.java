package com.integraal.ops.integration.flow;

import com.integraal.ops.integration.flow.beans.IssueHandlerInbean;
import com.integraal.ops.integration.transversal.services.LogicService;

public interface IssueHandlerService extends LogicService {
    void handleMessage(IssueHandlerInbean issueHandlerInbean);
}
