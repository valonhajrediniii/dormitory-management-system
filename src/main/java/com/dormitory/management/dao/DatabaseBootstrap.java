package com.dormitory.management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DatabaseBootstrap {
    private static final String CREATE_USERS_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGSERIAL PRIMARY KEY,
                full_name VARCHAR(150) NOT NULL,
                email VARCHAR(150) NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
                created_at TIMESTAMP NOT NULL DEFAULT NOW()
            )
            """;

    private static final String SEED_ADMIN_SQL = """
            INSERT INTO users (full_name, email, password, role)
            VALUES ('System Admin', 'admin@dormitory.local', 'admin123', 'ADMIN')
            ON CONFLICT (email) DO NOTHING
            """;

    private DatabaseBootstrap() {
    }

    public static void initializeCoreSchema() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement createUsersTable = connection.prepareStatement(CREATE_USERS_TABLE_SQL)) {
                createUsersTable.execute();
            }
            try (PreparedStatement seedAdmin = connection.prepareStatement(SEED_ADMIN_SQL)) {
                seedAdmin.executeUpdate();
            }
        }
    }
}