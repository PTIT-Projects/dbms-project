package vn.ptit.hrms.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@Configuration
@Profile("test")
public class WarehouseDatabaseTester {

    @Bean
    public CommandLineRunner testWarehouseTables(@Qualifier("warehouseDataSource") DataSource warehouseDataSource) {
        return args -> {
            System.out.println("=== Warehouse DB Table List ===");
            try (Connection conn = warehouseDataSource.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                // catalog may be null depending on driver, schemaPattern null matches all
                try (ResultSet rs = meta.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"})) {
                    while (rs.next()) {
                        String tableName = rs.getString("TABLE_NAME");
                        System.out.println(tableName);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching warehouse DB tables: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}