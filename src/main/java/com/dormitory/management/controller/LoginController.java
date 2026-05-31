package com.dormitory.management.controller;

import com.dormitory.management.model.Role;
import com.dormitory.management.service.AuthService;
import com.dormitory.management.service.JdbcAuthService;
import com.dormitory.management.util.AlertUtil;
import com.dormitory.management.util.I18n;
import com.dormitory.management.util.SceneManager;
import com.dormitory.management.util.UserSession;
import com.dormitory.management.util.ValidationUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    private final AuthService authService = new JdbcAuthService();

    @FXML
    private TextField loginEmailField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private void initialize() {
        languageComboBox.setItems(FXCollections.observableArrayList(I18n.localizedLanguageOptions()));
        languageComboBox.setValue(I18n.localizedLanguageForCurrentLocale());
    }

    @FXML
    private void onLogin() {
        //merr te dhenat e Login-it nga forma
        String email = loginEmailField.getText();
        String password = loginPasswordField.getText();

        //Kontrollon nese email dhe password jane valide
        if (!ValidationUtil.isValidEmail(email) || !ValidationUtil.hasText(password)) {
            AlertUtil.error(I18n.tr("alert.loginFailed.title"), I18n.tr("alert.loginFailed.invalidInput"));
            return;
        }

        //Pas Login-it perdoruesi dergohet ne dashboard sipas rolit
        authService.login(email.trim(), password)
                .ifPresentOrElse(user -> {
                    UserSession.setCurrentUser(user);
                    if (user.getRole() == Role.ADMIN) {
                        SceneManager.switchTo("admin-dashboard.fxml", "app.title.admin");
                    } else {
                        SceneManager.switchTo("user-dashboard.fxml", "app.title.user");
                    }
                }, () -> AlertUtil.error(I18n.tr("alert.loginFailed.title"), I18n.tr("alert.loginFailed.invalidCredentials")));
    }

    @FXML
    private void onOpenRegister() {
        SceneManager.switchTo("register.fxml", "app.title.register");
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
