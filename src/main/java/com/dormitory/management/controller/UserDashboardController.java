package com.dormitory.management.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.dormitory.management.model.Application;
import com.dormitory.management.model.ApplicationStatus;
import com.dormitory.management.model.StudentProfile;
import com.dormitory.management.model.User;
import com.dormitory.management.service.ApplicationService;
import com.dormitory.management.service.ComplaintService;
import com.dormitory.management.service.StudentProfileService;
import com.dormitory.management.util.AlertUtil;
import com.dormitory.management.util.OperationResult;
import com.dormitory.management.util.SceneManager;
import com.dormitory.management.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserDashboardController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String DUMMY_QR_PAYLOAD = "DORMITORY_ACCEPTED_DEMO_2026";
    private static final List<String> STATUS_STYLE_CLASSES = List.of(
            "status-pill-pending",
            "status-pill-accepted",
            "status-pill-rejected",
            "status-pill-neutral"
    );

    private final StudentProfileService studentProfileService = new StudentProfileService();
    private final ApplicationService applicationService = new ApplicationService();
    private final ComplaintService complaintService = new ComplaintService();

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label profileNameLabel;

    @FXML
    private Label profileStudentIdLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private TextField facultyField;

    @FXML
    private TextField studyProgramField;

    @FXML
    private TextField yearOfStudyField;

    @FXML
    private TextField genderField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField photoUrlField;

    @FXML
    private TabPane dashboardTabPane;

    @FXML
    private Tab applicationTab;

    @FXML
    private TextArea applicationNotesArea;

    @FXML
    private TextArea complaintMessageArea;

    @FXML
    private Label applicationStatusLabel;

    @FXML
    private Label applicationDateLabel;

    @FXML
    private Label dormitoryLabel;

    @FXML
    private Label roomLabel;

    @FXML
    private Label statusMessageLabel;

    @FXML
    private ImageView qrImageView;

    @FXML
    private Label qrHintLabel;

    @FXML
    private void initialize() {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            AlertUtil.error("Session Expired", "Please login again.");
            SceneManager.switchTo("login.fxml", "Dormitory Management System - Login");
            return;
        }

        welcomeLabel.setText("Welcome, " + currentUser.getFullName());
        profileNameLabel.setText(currentUser.getFullName());
        profileStudentIdLabel.setText(currentUser.getStudentId() == null ? "-" : currentUser.getStudentId());
        profileImageView.setImage(createPlaceholderAvatar());
        loadProfile(currentUser.getId());
        loadApplicationStatus(currentUser.getId());
    }