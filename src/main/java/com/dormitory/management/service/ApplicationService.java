package com.dormitory.management.service;

import com.dormitory.management.dao.ApplicationDao;
import com.dormitory.management.dao.StudentProfileDao;
import com.dormitory.management.model.Application;
import com.dormitory.management.model.ApplicationStatus;
import com.dormitory.management.util.OperationResult;

import java.util.Optional;

public class ApplicationService {
    private final ApplicationDao applicationDao = new ApplicationDao();
    private final StudentProfileDao studentProfileDao = new StudentProfileDao();

    public Optional<Application> getByUserId(long userId) {
        return applicationDao.findByUserId(userId);
    }

    public OperationResult submitApplication(long userId, String notes) {
        if (studentProfileDao.findByUserId(userId).isEmpty()) {
            return OperationResult.failure("service.application.profileRequired");
        }

        Optional<Application> existing = applicationDao.findByUserId(userId);
        if (existing.isPresent()) {
            ApplicationStatus status = existing.get().getStatus();
            if (status == ApplicationStatus.PENDING || status == ApplicationStatus.ACCEPTED) {
                return OperationResult.failure("service.application.activeExists");
            }
            return OperationResult.failure("service.application.alreadySubmitted");
        }

        boolean created = applicationDao.createPending(userId, notes == null ? "" : notes.trim());
        return created
            ? OperationResult.success("service.application.success")
            : OperationResult.failure("service.application.error");
    }
}
