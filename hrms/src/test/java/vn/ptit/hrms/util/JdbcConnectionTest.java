package vn.ptit.hrms.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class JdbcConnectionTest {

    @Bean
    public CommandLineRunner testJdbcConnection() {
        return args -> {
            System.out.println("Testing JDBC connection...");

            String url = "jdbc:sqlserver://localhost;databaseName=nhansucongty;trustServerCertificate=true";
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

                    // Get all table names
                    List<String> tables = new ArrayList<>();
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet rs = metaData.getTables(null, "dbo", "%", new String[] {"TABLE"});

                    while (rs.next()) {
                        tables.add(rs.getString("TABLE_NAME"));
                    }

                    // Print data from each table
                    for (String tableName : tables) {
                        System.out.println("\n=== Data from table: " + tableName + " ===");
                        try (Statement stmt = connection.createStatement();
                             ResultSet tableData = stmt.executeQuery("SELECT TOP 5 * FROM " + tableName)) {

                            // Print column names
                            int columnCount = tableData.getMetaData().getColumnCount();
                            StringBuilder header = new StringBuilder();
                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = tableData.getMetaData().getColumnName(i);
                                header.append(columnName).append("\t");
                            }
                            System.out.println(header.toString());

                            // Print data rows
                            while (tableData.next()) {
                                StringBuilder row = new StringBuilder();
                                for (int i = 1; i <= columnCount; i++) {
                                    Object value = tableData.getObject(i);
                                    row.append(value != null ? value.toString() : "NULL").append("\t");
                                }
                                System.out.println(row.toString());
                            }
                        } catch (Exception e) {
                            System.out.println("Could not print data for table " + tableName + ": " + e.getMessage());
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