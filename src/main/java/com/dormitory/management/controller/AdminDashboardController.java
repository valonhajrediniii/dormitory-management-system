package com.dormitory.management.controller;

import com.dormitory.management.model.AcceptedStudentView;
import com.dormitory.management.model.AdminComplaintView;
import com.dormitory.management.model.PendingApplicationView;
import com.dormitory.management.model.User;
import com.dormitory.management.service.AdminService;
import com.dormitory.management.util.AlertUtil;
import com.dormitory.management.util.OperationResult;
import com.dormitory.management.util.SceneManager;
import com.dormitory.management.util.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDashboardController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AdminService adminService = new AdminService();

    @FXML
    private Label adminWelcomeLabel;

    @FXML
    private TableView<PendingApplicationView> applicationsTable;

    @FXML
    private TableColumn<PendingApplicationView, String> studentNameColumn;

    @FXML
    private TableColumn<PendingApplicationView, String> studentEmailColumn;

    @FXML
    private TableColumn<PendingApplicationView, String> facultyColumn;

    @FXML
    private TableColumn<PendingApplicationView, Integer> yearColumn;

    @FXML
    private TableColumn<PendingApplicationView, String> dateColumn;







}
