package com.integraal.ops.integration.model.repositories;

import com.integraal.ops.integration.model.persistence.FlowException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlowExceptionsRepository extends JpaRepository<FlowException, UUID> {
}
