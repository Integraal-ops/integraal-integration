package com.integraal.ops.integration.model.repositories;

import com.integraal.ops.integration.model.persistence.FlowException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlowExceptionsRepository extends JpaRepository<FlowException, UUID> {
}
