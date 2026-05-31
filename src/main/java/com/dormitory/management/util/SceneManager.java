package com.dormitory.management.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class SceneManager {
    //Ruan stage kryesor qe perdoret per nderrimin e faqeve
    private static Stage primaryStage;
    private static String currentFxmlFileName;
    private static String currentTitleKey;
    private static Object[] currentTitleArgs = new Object[0];

    private SceneManager() {
    }

    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlFileName, String titleKey, Object... titleArgs) {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage is not initialized.");
        }

        try {
            //Ngarkon FXML-in dhe lidh Css per pamjen e re
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(SceneManager.class.getResource("/fxml/" + fxmlFileName)),
                    I18n.bundle());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(
                    SceneManager.class.getResource("/css/styles.css")).toExternalForm());

            //Nderron titullin dhe skenen aktuale
            primaryStage.setTitle(I18n.tr(titleKey, titleArgs));
            primaryStage.setScene(scene);
            primaryStage.show();

            currentFxmlFileName = fxmlFileName;
            currentTitleKey = titleKey;
            currentTitleArgs = titleArgs == null ? new Object[0] : titleArgs.clone();
        } catch (IOException e) {
            throw new IllegalStateException(I18n.tr("app.error.fxmlLoad", fxmlFileName), e);
        }
    }

    public static void reloadCurrentScene() {
        if (currentFxmlFileName == null || currentTitleKey == null) {
            return;
        }
        switchTo(currentFxmlFileName, currentTitleKey, currentTitleArgs);
    }
}