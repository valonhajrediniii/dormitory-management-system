package com.dormitory.management.controller;

import com.dormitory.management.service.AuthService;
import com.dormitory.management.service.JdbcAuthService;
import com.dormitory.management.util.AlertUtil;
import com.dormitory.management.util.I18n;
import com.dormitory.management.util.SceneManager;
import com.dormitory.management.util.ValidationUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    private final AuthService authService = new JdbcAuthService();

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField studentIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private void initialize() {
        languageComboBox.setItems(FXCollections.observableArrayList(I18n.localizedLanguageOptions()));
        languageComboBox.setValue(I18n.localizedLanguageForCurrentLocale());
    }

    @FXML
    private void onRegister() {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String studentId = studentIdField.getText();
        String password = passwordField.getText();

        if (!ValidationUtil.hasText(fullName)
                || !ValidationUtil.hasText(studentId)
                || !ValidationUtil.isValidEmail(email)
                || !ValidationUtil.minLength(password, 6)) {
            AlertUtil.error(I18n.tr("alert.registerFailed.title"), I18n.tr("alert.registerFailed.message"));
            return;
        }

        boolean registered = authService.register(fullName.trim(), studentId.trim(), email.trim(), password);
        if (registered) {
            AlertUtil.info(I18n.tr("alert.registerSuccess.title"), I18n.tr("alert.registerSuccess.message"));
            SceneManager.switchTo("login.fxml", "app.title.login");
        } else {
            AlertUtil.error(I18n.tr("alert.registerFailed.title"), I18n.tr("alert.registerFailed.exists"));
        }
    }

    @FXML
    private void onBackToLogin() {
        SceneManager.switchTo("login.fxml", "app.title.login");
    }

    @FXML
    private void onLanguageChanged() {
        String value = languageComboBox.getValue();
        if (value == null) {
            return;
        }

        I18n.setLocale(I18n.localeFromSelection(value));
        SceneManager.reloadCurrentScene();
    }
}