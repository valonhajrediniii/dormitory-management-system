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

    @FXML
    private void onSaveProfile() {
        User user = UserSession.getCurrentUser();
        if (user == null) {
            AlertUtil.error("Session Expired", "Please login again.");
            return;
        }

        OperationResult result = saveProfileForUser(user);
        if (result.isSuccess()) {
            AlertUtil.info("Profile", result.getMessage());
        } else {
            AlertUtil.error("Profile", result.getMessage());
        }
    }

    @FXML
    private void onPreviewPhoto() {
        updateProfileImage(photoUrlField.getText());
    }

    @FXML
    private void onChoosePhoto() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Profile Photo");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.webp")
        );

        String currentPath = photoUrlField.getText();
        if (currentPath != null && !currentPath.isBlank()) {
            File selected = new File(currentPath.trim());
            File parent = selected.isDirectory() ? selected : selected.getParentFile();
            if (parent != null && parent.exists()) {
                chooser.setInitialDirectory(parent);
            }
        }

        File file = chooser.showOpenDialog(photoUrlField.getScene().getWindow());
        if (file == null) {
            return;
        }

        photoUrlField.setText(file.getAbsolutePath());
        updateProfileImage(file.getAbsolutePath());
    }

    @FXML
    private void onSubmitApplication() {
        User user = UserSession.getCurrentUser();
        if (user == null) {
            AlertUtil.error("Session Expired", "Please login again.");
            return;
        }

        String notes = applicationNotesArea.getText();
        if (notes != null && notes.length() > 1000) {
            AlertUtil.error("Validation Error", "Application notes can have up to 1000 characters.");
            return;
        }

        if (studentProfileService.getByUserId(user.getId()).isEmpty()) {
            OperationResult saveResult = saveProfileForUser(user);
            if (!saveResult.isSuccess()) {
                AlertUtil.error("Application", "Please complete and save your profile before applying. " + saveResult.getMessage());
                dashboardTabPane.getSelectionModel().select(0);
                return;
            }
            AlertUtil.info("Profile", "Profile saved. You can now submit your application.");
        }

        OperationResult result = applicationService.submitApplication(user.getId(), notes);
        if (result.isSuccess()) {
            AlertUtil.info("Application", result.getMessage());
            loadApplicationStatus(user.getId());
        } else {
            AlertUtil.error("Application", result.getMessage());
        }
    }

    @FXML
    private void onRefreshStatus() {
        User user = UserSession.getCurrentUser();
        if (user != null) {
            loadApplicationStatus(user.getId());
        }
    }