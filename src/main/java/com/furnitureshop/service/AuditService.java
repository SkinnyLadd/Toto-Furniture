package com.furnitureshop.service;

import com.furnitureshop.model.entity.AuditLog;
import com.furnitureshop.model.entity.User;
import com.furnitureshop.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuditLog logActivity(String action, String entityType, Long entityId, String details, User user, String ipAddress) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setUser(user);
        auditLog.setIpAddress(ipAddress);
        
        return auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findAuditLogsByUser(User user) {
        return auditLogRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findAuditLogsByEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findAuditLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> findAuditLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }
}
