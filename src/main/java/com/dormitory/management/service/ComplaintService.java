package com.dormitory.management.service;

import com.dormitory.management.dao.ApplicationDao;
import com.dormitory.management.dao.ComplaintDao;
import com.dormitory.management.model.AdminComplaintView;
import com.dormitory.management.model.Application;
import com.dormitory.management.model.ApplicationStatus;
import com.dormitory.management.util.OperationResult;

import java.util.List;
import java.util.Optional;

public class ComplaintService {
    private static final int MAX_COMPLAINT_LENGTH = 2000;

    private final ComplaintDao complaintDao = new ComplaintDao();
    private final ApplicationDao applicationDao = new ApplicationDao();

    public OperationResult submitComplaint(long userId, String message) {
        String trimmedMessage = message == null ? "" : message.trim();
        if (trimmedMessage.isEmpty()) {
            return OperationResult.failure("service.complaint.required");
        }

        if (trimmedMessage.length() > MAX_COMPLAINT_LENGTH) {
            return OperationResult.failure("service.complaint.maxLength", MAX_COMPLAINT_LENGTH);
        }

        Optional<Application> applicationOptional = applicationDao.findByUserId(userId);
        if (applicationOptional.isEmpty()) {
            return OperationResult.failure("service.complaint.assignmentRequired");
        }

        Application application = applicationOptional.get();
        if (application.getStatus() != ApplicationStatus.ACCEPTED || application.getDormitoryId() == null) {
            return OperationResult.failure("service.complaint.onlyAfterAssignment");
        }

        boolean created = complaintDao.createComplaint(userId, application.getDormitoryId(), trimmedMessage);
        return created
                ? OperationResult.success("service.complaint.success")
                : OperationResult.failure("service.complaint.error");
    }

    public List<AdminComplaintView> getComplaintsForAdmin() {
        return complaintDao.findAllForAdmin();
    }
}
