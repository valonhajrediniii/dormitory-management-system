package com.dormitory.management.service;

import com.dormitory.management.dao.ApplicationDao;
import com.dormitory.management.dao.ComplaintDao;
import com.dormitory.management.dao.DatabaseConnection;
import com.dormitory.management.dao.RoomDao;
import com.dormitory.management.dao.StudentProfileDao;
import com.dormitory.management.model.AdminComplaintView;
import com.dormitory.management.model.AdminStudentProfileDetails;
import com.dormitory.management.model.AcceptedStudentView;
import com.dormitory.management.model.PendingApplicationView;
import com.dormitory.management.model.Room;
import com.dormitory.management.model.RoomStatus;
import com.dormitory.management.model.StudentProfile;
import com.dormitory.management.util.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    private final ApplicationDao applicationDao = new ApplicationDao();
    private final RoomDao roomDao = new RoomDao();
    private final ComplaintDao complaintDao = new ComplaintDao();
    private final StudentProfileDao studentProfileDao = new StudentProfileDao();

    public List<PendingApplicationView> getPendingApplications() {
        return applicationDao.findPendingForAdmin();
    }

    public List<AcceptedStudentView> getAcceptedStudents() {
        return applicationDao.findAcceptedForAdmin();
    }

    public AdminStudentProfileDetails getAcceptedStudentProfileDetails(AcceptedStudentView acceptedStudent) {
        if (acceptedStudent == null) {
            throw new IllegalArgumentException("Accepted student selection is required.");
        }

        StudentProfile profile = null;
        if (acceptedStudent.getUserId() != null) {
            profile = studentProfileDao.findByUserId(acceptedStudent.getUserId()).orElse(null);
        }

        return AdminStudentProfileDetails.from(acceptedStudent, profile);
    }

    public List<Room> getAssignableRooms() {
        return roomDao.findAssignableRooms();
    }

    public List<AdminComplaintView> getComplaintsForAdmin() {
        return complaintDao.findAllForAdmin();
    }

    public OperationResult rejectApplication(long applicationId) {
        boolean updated = applicationDao.markRejected(applicationId);
        return updated
                ? OperationResult.success("service.admin.reject.success")
                : OperationResult.failure("service.admin.reject.error");
    }

    public OperationResult approveApplication(long applicationId, long roomId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            boolean previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try {
                Optional<Room> roomOptional = roomDao.findByIdForUpdate(connection, roomId);
                if (roomOptional.isEmpty()) {
                    connection.rollback();
                    return OperationResult.failure("service.admin.approve.roomMissing");
                }

                Room room = roomOptional.get();
                if (room.getOccupiedBeds() >= room.getCapacity()) {
                    connection.rollback();
                    return OperationResult.failure("service.admin.approve.roomFull");
                }

                int newOccupiedBeds = room.getOccupiedBeds() + 1;
                RoomStatus newStatus = resolveStatus(newOccupiedBeds, room.getCapacity());

                applicationDao.markAccepted(connection, applicationId, room.getDormitoryId(), room.getId());
                roomDao.updateOccupancy(connection, room.getId(), newOccupiedBeds, newStatus);

                connection.commit();
                return OperationResult.success("service.admin.approve.success");
            } catch (SQLException ex) {
                connection.rollback();
                LOGGER.error("Failed approving application {} with room {}", applicationId, roomId, ex);
                return OperationResult.failure("service.admin.approve.dbError");
            } finally {
                connection.setAutoCommit(previousAutoCommit);
            }
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Approval transaction setup failed", ex);
            return OperationResult.failure("service.admin.approve.error");
        }
    }

    private RoomStatus resolveStatus(int occupiedBeds, int capacity) {
        if (occupiedBeds <= 0) {
            return RoomStatus.FREE;
        }
        if (occupiedBeds >= capacity) {
            return RoomStatus.FULL;
        }
        return RoomStatus.PARTIAL;
    }
}
