package com.integraal.ops.integration.flow.statemachine;

public class RoutingStateConstants {
    // ? =========<  Routing Invalidity Reason  >=======
    public static final String NULL_SAFETY_NOT_CHECKED = "Null safety not respected";
    public static final String ROUTING_WITH_UNFILLED_FLOW_KEY_ID = "[ROUTING] Routing With Unfilled Flow Key Id";
    public static final String ROUTING_WITH_UNFILLED_FLOW_ID = "[ROUTING] Routing With Unfilled Flow Id";
    public static final String ROUTING_WITH_EXCEPTION = "[ROUTING] Routing Step but with exception filled";
    public static final String ROUTING_ENTRYPOINT_BUT_ORIGIN_STEP = "[ROUTING][ENTRYPOINT] Routing Entrypoint but Origin step present";
    public static final String ROUTING_INSTEP_BUT_ORIGIN_STEP = "[ROUTING][IN_STEP] Routing In Step but Origin step Absent";
    public static final String DISCOVERY_PROCESS_WITH_EXCEPTION = "[DISCOVERY] Discovery process but with exception filled";
    public static final String ISSUE_WITHOUT_EXCEPTION_REGISTERED = "[ISSUE] Routing Issue but without exception registered";
    public static final String ISSUE_WITHOUT_FLOW_ID = "[ISSUE] Routing Issue but without Flow ID";
    public static final String ROUTING_UNKNOWN_ROUTING_TYPE = "[ROUTING] Unknown routing type";
}
