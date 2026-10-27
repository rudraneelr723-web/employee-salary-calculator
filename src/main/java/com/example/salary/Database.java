package com.example.salary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String URL = System.getenv().getOrDefault(
            "DB_URL", "jdbc:mysql://localhost:3306/employee_salary_db?useSSL=false&serverTimezone=Asia/Kolkata");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
