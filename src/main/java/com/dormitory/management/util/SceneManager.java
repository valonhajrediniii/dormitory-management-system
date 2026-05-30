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

    private SceneManager() {
    }

    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlFileName, String title) {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage is not initialized.");
        }

        try {
            //Ngarkon FXML-in dhe lidh Css per pamjen e re
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    SceneManager.class.getResource("/fxml/" + fxmlFileName)));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(
                    SceneManager.class.getResource("/css/styles.css")).toExternalForm());

            //Nderron titullin dhe skenen aktuale
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load FXML: " + fxmlFileName, e);
        }
    }
}