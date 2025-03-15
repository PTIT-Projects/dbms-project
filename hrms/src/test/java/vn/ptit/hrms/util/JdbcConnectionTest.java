package vn.ptit.hrms.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Configuration
public class JdbcConnectionTest {

    @Bean
    public CommandLineRunner testJdbcConnection() {
        return args -> {
            System.out.println("Testing JDBC connection...");

            String url = "jdbc:sqlserver://localhost;databaseName=hrms;trustServerCertificate=true";
            String username = "test_user";
            String password = "superSecretPassword!123";

            try {
                // Load the SQL Server JDBC driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Attempt to establish a connection
                try (Connection connection = DriverManager.getConnection(url, username, password)) {
                    System.out.println("✅ Database connection successful!");

                    // Execute a simple query to verify the connection
                    try (Statement stmt = connection.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT @@VERSION AS 'SQL Server Version'")) {

                        if (rs.next()) {
                            System.out.println("SQL Server Version: " + rs.getString("SQL Server Version"));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Database connection failed!");
                e.printStackTrace();
            }
        };
    }
}