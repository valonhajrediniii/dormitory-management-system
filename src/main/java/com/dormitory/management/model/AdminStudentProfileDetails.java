package com.dormitory.management.model;

import java.time.LocalDateTime;

public class AdminStudentProfileDetails {
    private final String fullName;
    private final String email;
    private final String faculty;
    private final String studyProgram;
    private final Integer yearOfStudy;
    private final String gender;
    private final String phone;
    private final String city;
    private final String photoUrl;
    private final String dormitoryNumber;
    private final String roomNumber;
    private final LocalDateTime applicationDate;

    public AdminStudentProfileDetails(
            String fullName,
            String email,
            String faculty,
            String studyProgram,
            Integer yearOfStudy,
            String gender,
            String phone,
            String city,
            String photoUrl,
            String dormitoryNumber,
            String roomNumber,
            LocalDateTime applicationDate) {
        this.fullName = fullName;
        this.email = email;
        this.faculty = faculty;
        this.studyProgram = studyProgram;
        this.yearOfStudy = yearOfStudy;
        this.gender = gender;
        this.phone = phone;
        this.city = city;
        this.photoUrl = photoUrl;
        this.dormitoryNumber = dormitoryNumber;
        this.roomNumber = roomNumber;
        this.applicationDate = applicationDate;
    }

    public static AdminStudentProfileDetails from(AcceptedStudentView acceptedStudent, StudentProfile profile) {
        String faculty = acceptedStudent.getFaculty();
        Integer yearOfStudy = acceptedStudent.getYearOfStudy();
        String studyProgram = null;
        String gender = null;
        String phone = null;
        String city = null;
        String photoUrl = null;

        if (profile != null) {
            if (profile.getFaculty() != null && !profile.getFaculty().isBlank()) {
                faculty = profile.getFaculty();
            }
            if (profile.getYearOfStudy() > 0) {
                yearOfStudy = profile.getYearOfStudy();
            }
            studyProgram = profile.getStudyProgram();
            gender = profile.getGender();
            phone = profile.getPhone();
            city = profile.getCity();
            photoUrl = profile.getPhotoUrl();
        }

        return new AdminStudentProfileDetails(
                acceptedStudent.getStudentName(),
                acceptedStudent.getStudentEmail(),
                faculty,
                studyProgram,
                yearOfStudy,
                gender,
                phone,
                city,
                photoUrl,
                acceptedStudent.getDormitoryNumber(),
                acceptedStudent.getRoomNumber(),
                acceptedStudent.getApplicationDate());
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getDormitoryNumber() {
        return dormitoryNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }
}