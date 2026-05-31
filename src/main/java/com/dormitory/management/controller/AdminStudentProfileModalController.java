package com.dormitory.management.controller;

import com.dormitory.management.model.AdminStudentProfileDetails;
import com.dormitory.management.util.I18n;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class AdminStudentProfileModalController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label fullNameValueLabel;

    @FXML
    private Label emailValueLabel;

    @FXML
    private Label facultyValueLabel;

    @FXML
    private Label studyProgramValueLabel;

    @FXML
    private Label yearValueLabel;

    @FXML
    private Label genderValueLabel;

    @FXML
    private Label phoneValueLabel;

    @FXML
    private Label cityValueLabel;

    @FXML
    private Label dormitoryValueLabel;

    @FXML
    private Label roomValueLabel;

    @FXML
    private Label acceptedAtValueLabel;

    @FXML
    private void initialize() {
        profileImageView.setImage(createPlaceholderAvatar());
    }

    public void setDetails(AdminStudentProfileDetails details) {
        fullNameValueLabel.setText(orNone(details.getFullName()));
        emailValueLabel.setText(orNone(details.getEmail()));
        facultyValueLabel.setText(orNone(details.getFaculty()));
        studyProgramValueLabel.setText(orNone(details.getStudyProgram()));
        yearValueLabel.setText(details.getYearOfStudy() == null ? I18n.tr("status.none") : String.valueOf(details.getYearOfStudy()));
        genderValueLabel.setText(orNone(details.getGender()));
        phoneValueLabel.setText(orNone(details.getPhone()));
        cityValueLabel.setText(orNone(details.getCity()));
        dormitoryValueLabel.setText(orNone(details.getDormitoryNumber()));
        roomValueLabel.setText(orNone(details.getRoomNumber()));
        acceptedAtValueLabel.setText(details.getApplicationDate() == null
                ? I18n.tr("status.none")
                : details.getApplicationDate().format(DATE_TIME_FORMATTER));

        updateProfileImage(details.getPhotoUrl());
    }

    @FXML
    private void onClose() {
        profileImageView.getScene().getWindow().hide();
    }

    private String orNone(String value) {
        return (value == null || value.isBlank()) ? I18n.tr("status.none") : value;
    }

    private void updateProfileImage(String photoUrl) {
        if (photoUrl == null || photoUrl.isBlank()) {
            profileImageView.setImage(createPlaceholderAvatar());
            return;
        }

        try {
            String source = photoUrl.trim();
            if (!source.startsWith("http://") && !source.startsWith("https://") && !source.startsWith("file:")) {
                source = new File(source).toURI().toString();
            }

            Image image = new Image(source, true);
            if (image.isError()) {
                profileImageView.setImage(createPlaceholderAvatar());
            } else {
                profileImageView.setImage(image);
            }
        } catch (RuntimeException ex) {
            profileImageView.setImage(createPlaceholderAvatar());
        }
    }

    private WritableImage createPlaceholderAvatar() {
        int size = 140;
        WritableImage image = new WritableImage(size, size);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x < 6 || y < 6 || x >= size - 6 || y >= size - 6) {
                    image.getPixelWriter().setColor(x, y, Color.web("#1e4d8a"));
                } else {
                    image.getPixelWriter().setColor(x, y, Color.web("#dbe8ff"));
                }
            }
        }
        return image;
    }
}