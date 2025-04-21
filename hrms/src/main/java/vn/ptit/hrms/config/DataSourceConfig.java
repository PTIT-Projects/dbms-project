package vn.ptit.hrms.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class DataSourceConfig {

    // Primary datasource properties
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Warehouse datasource properties
    @Bean
    @ConfigurationProperties(prefix = "spring.second-datasource")
    public DataSourceProperties warehouseDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Primary DataSource
    @Bean
    @Primary
    public DataSource primaryDataSource(
            @Qualifier("primaryDataSourceProperties") DataSourceProperties props) {
        return props.initializeDataSourceBuilder().build();
    }

    // Warehouse DataSource
    @Bean
    public DataSource warehouseDataSource(
            @Qualifier("warehouseDataSourceProperties") DataSourceProperties props) {
        return props.initializeDataSourceBuilder()
                   .type(org.springframework.jdbc.datasource.DriverManagerDataSource.class)
                   .build();
    }

    // JdbcTemplate beans
    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate(
            @Qualifier("primaryDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean
    public JdbcTemplate warehouseJdbcTemplate(
            @Qualifier("warehouseDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    // Transaction manager for warehouse
    @Bean
    public PlatformTransactionManager warehouseTransactionManager(
            @Qualifier("warehouseDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    public TransactionTemplate warehouseTransactionTemplate(
            @Qualifier("warehouseTransactionManager") PlatformTransactionManager txMgr) {
        return new TransactionTemplate(txMgr);
    }
}