package com.dormitory.management.service;

import com.dormitory.management.dao.DatabaseConnection;
import com.dormitory.management.model.Role;
import com.dormitory.management.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * JDBC-based implementation of the authentication service.
 * This class communicates with the database to perform login and registration operations.
 */
public class JdbcAuthService implements AuthService {

    /**
     * Validates user credentials by checking the users table in the database.
     * If a matching user is found, the database record is mapped into a User object.
     */
    @Override
    public Optional<User> login(String email, String password) {
        String sql = """
                SELECT id, full_name, email, password, role, created_at
                FROM users
                WHERE email = ? AND password = ?
                LIMIT 1
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setFullName(resultSet.getString("full_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(Role.valueOf(resultSet.getString("role")));

                    Timestamp createdAt = resultSet.getTimestamp("created_at");
                    user.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : LocalDateTime.now());
                    return Optional.of(user);
                }
            }
        } catch (SQLException | IllegalStateException ex) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    /**
     * Inserts a new user record into the database with the default USER role.
     * PreparedStatement is used to pass user input safely into the SQL query.
     */
    @Override
    public boolean register(String fullName, String studentId, String email, String password) {
        String sql = """
                INSERT INTO users (full_name, student_id, email, password, role)
                VALUES (?, ?, ?, ?, 'USER')
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fullName);
            statement.setString(2, studentId);
            statement.setString(3, email);
            statement.setString(4, password);

            return statement.executeUpdate() == 1;
        } catch (SQLException | IllegalStateException ex) {
            return false;
        }
    }
}