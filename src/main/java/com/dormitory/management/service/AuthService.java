package com.dormitory.management.service;

import com.dormitory.management.model.User;

import java.util.Optional;

/**
 * Defines the authentication contract for the dormitory management system.
 * This interface declares the main operations required for user login and registration.
 */
public interface AuthService {

    /**
     * Authenticates a user using email and password.
     *
     * @param email user's email address
     * @param password user's password
     * @return an Optional containing the user if authentication succeeds, otherwise empty
     */
    Optional<User> login(String email, String password);

    /**
     * Registers a new user account in the system.
     *
     * @param fullName user's full name
     * @param studentId user's student identifier
     * @param email user's email address
     * @param password user's password
     * @return true if registration succeeds, otherwise false
     */
    boolean register(String fullName, String studentId, String email, String password);
}