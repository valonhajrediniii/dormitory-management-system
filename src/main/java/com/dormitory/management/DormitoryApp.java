package com.dormitory.management;

import com.dormitory.management.dao.DatabaseBootstrap;
import com.dormitory.management.util.AlertUtil;
import com.dormitory.management.util.I18n;
import com.dormitory.management.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DormitoryApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            DatabaseBootstrap.initializeCoreSchema();
        } catch (SQLException | IllegalStateException ex) {
            String details = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
            AlertUtil.error(I18n.tr("app.error.db.title"), I18n.tr("app.error.db.message", details));
            return;
        }

        SceneManager.initialize(stage);
        SceneManager.switchTo("login.fxml", "app.title.main");
    }

    public static void main(String[] args) {
        launch(args);
    }
}