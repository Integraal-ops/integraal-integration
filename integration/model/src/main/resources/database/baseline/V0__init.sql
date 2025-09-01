CREATE TYPE STEP_STATUS as ENUM ('IN_PROGRESS','COMPLETED','FAILED');

CREATE TABLE process_step
(
    id           UUID                        NOT NULL,
    step_status  STEP_STATUS,
    step_id      UUID,
    step_key_id  UUID,
    flow_id      UUID,
    flow_key_id  UUID,
    data_id      VARCHAR(255),
    exception_id UUID,
    start_time   TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time     TIMESTAMP WITH TIME ZONE,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_process_step PRIMARY KEY (id)
);


CREATE TABLE flow_exception
(
    id             UUID                        NOT NULL,
    exception_type VARCHAR(255),
    message        VARCHAR(1024),
    stack_trace    TEXT,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_flow_exceptions PRIMARY KEY (id)
);