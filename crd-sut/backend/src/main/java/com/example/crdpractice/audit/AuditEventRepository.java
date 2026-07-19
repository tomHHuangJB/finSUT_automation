package com.example.crdpractice.audit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface AuditEventRepository extends JpaRepository<AuditEventEntity, UUID> {
}
