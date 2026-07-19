package com.example.crdpractice.audit;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditEventRepository auditEventRepository;

    AuditService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    public void record(String entityType, UUID entityId, String action, String oldValue, String newValue, String performedBy, String correlationId) {
        auditEventRepository.save(new AuditEventEntity(
                entityType,
                entityId,
                action,
                oldValue,
                newValue,
                performedBy,
                correlationId));
    }
}
