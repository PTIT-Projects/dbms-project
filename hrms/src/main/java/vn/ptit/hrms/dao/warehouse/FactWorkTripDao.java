package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.FactWorkTrip;
import vn.ptit.hrms.constant.FactWorkTripStatusEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vn.ptit.hrms.mapper.warehouse.FactWorkTripRowMapper;

@Repository
public class FactWorkTripDao {

    private final JdbcTemplate jdbcTemplate;

    public FactWorkTripDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 4.a: Nhân viên có nhiều chuyến công tác nhất
    public List<FactWorkTrip> getTop10EmployeesByTrips() {
        String sql = "SELECT  " +
                "    employee_name, " +
                "    department_name, " +
                "    position_name, " +
                "    COUNT(*) AS total_trips, " +
                "    SUM(trip_duration) AS total_days_on_trip, " +
                "    SUM(total_cost) AS total_trip_cost " +
                "FROM fact_work_trips " +
                "WHERE status = 'Đã hoàn thành' " +
                "GROUP BY employee_name, department_name, position_name " +
                "ORDER BY total_trips DESC, total_days_on_trip DESC " +
                "LIMIT 10";
        return jdbcTemplate.query(sql, new FactWorkTripRowMapper());
    }

    // 4.b: Điểm đến công tác phổ biến
    public List<FactWorkTrip> getTop10Destinations() {
        String sql = "SELECT  " +
                "    destination, " +
                "    COUNT(*) AS trip_count, " +
                "    COUNT(DISTINCT employee_name) AS distinct_employees, " +
                "    SUM(total_cost) AS total_cost, " +
                "    AVG(total_cost) AS avg_cost_per_trip, " +
                "    AVG(trip_duration) AS avg_duration " +
                "FROM fact_work_trips " +
                "WHERE status = 'Đã hoàn thành' " +
                "GROUP BY destination " +
                "ORDER BY trip_count DESC " +
                "LIMIT 10";
        return jdbcTemplate.query(sql, new FactWorkTripRowMapper());
    }

    // 4.c: Tổng chi phí công tác theo phòng ban
    public List<FactWorkTrip> getTotalCostByDepartment() {
        String sql = "SELECT  " +
                "    department_name, " +
                "    COUNT(*) AS total_trips, " +
                "    SUM(trip_duration) AS total_days, " +
                "    SUM(total_cost) AS total_cost, " +
                "    SUM(total_cost) / SUM(trip_duration) AS cost_per_day, " +
                "    SUM(total_cost) / COUNT(DISTINCT employee_name) AS cost_per_employee " +
                "FROM fact_work_trips " +
                "WHERE status = 'Đã hoàn thành' " +
                "GROUP BY department_name " +
                "ORDER BY total_cost DESC";
        return jdbcTemplate.query(sql, new FactWorkTripRowMapper());
    }

    // 4.d: Phân tích theo mục đích công tác
    public List<FactWorkTrip> getAnalysisByPurpose() {
        String sql = "SELECT  " +
                "    purpose, " +
                "    COUNT(*) AS trip_count, " +
                "    COUNT(DISTINCT destination) AS distinct_destinations, " +
                "    AVG(trip_duration) AS avg_duration, " +
                "    SUM(total_cost) AS total_cost, " +
                "    SUM(total_cost) / COUNT(*) AS avg_cost_per_trip " +
                "FROM fact_work_trips " +
                "WHERE status = 'Đã hoàn thành' " +
                "GROUP BY purpose " +
                "ORDER BY total_cost DESC";
        return jdbcTemplate.query(sql, new FactWorkTripRowMapper());
    }

    // 4.e: Xu hướng công tác theo tháng
    public List<FactWorkTrip> getTripsByMonth() {
        String sql = "SELECT  " +
                "    dd.year, " +
                "    dd.month, " +
                "    COUNT(*) AS trip_count, " +
                "    SUM(fwt.total_cost) AS monthly_cost, " +
                "    SUM(fwt.trip_duration) AS total_days " +
                "FROM fact_work_trips fwt " +
                "JOIN dim_date dd ON fwt.start_date_sk = dd.date_sk " +
                "WHERE fwt.status = 'Đã hoàn thành' " +
                "GROUP BY dd.year, dd.month " +
                "ORDER BY dd.year, dd.month";
        return jdbcTemplate.query(sql, new FactWorkTripRowMapper());
    }

    // 4.f: Chi phí công tác theo phòng ban
    public List<FactWorkTrip> getCostByDepartment() {
        String sql = "SELECT  " +
                "    department_name, " +
                "    COUNT(DISTINCT employee_name) AS employees_with_trips, " +
                "    SUM(total_cost) AS total_cost, " +
                "    SUM(trip_duration) AS total_days, " +
                "    SUM(total_cost) / SUM(trip_duration) AS cost_per_day, " +
                "    SUM(total_cost) / COUNT(*) AS cost_per_trip " +
                "FROM fact_work_trips " +
                "WHERE status = 'Đã hoàn thành'  " +
                "GROUP BY department_name " +
                "ORDER BY cost_per_day DESC";
        return jdbcTemplate.query(sql, new FactWorkTripRowMapper());
    }

    // 4.g: Chi phí công tác theo vị trí/chức vụ
    public List<FactWorkTrip> getCostByPosition() {
        String sql = "SELECT  " +
                "    position_name, " +
                "    COUNT(*) AS trip_count, " +
                "    SUM(total_cost) AS total_cost, " +
                "    AVG(total_cost) AS avg_cost_per_trip, " +
                "    AVG(trip_duration) AS avg_duration " +
                "FROM fact_work_trips " +
                "WHERE status = 'Đã hoàn thành' " +
                "GROUP BY position_name " +
                "ORDER BY total_cost DESC";
        return jdbcTemplate.query(sql, new FactWorkTripRowMapper());
    }

    //Added Configuration class
    /*@Configuration
    public static class SpringConfig {

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");  // Or your specific driver
            dataSource.setUrl("jdbc:mysql://localhost:3306/your_database"); // Replace with your URL
            dataSource.setUsername("your_username");  // Replace with your username
            dataSource.setPassword("your_password");        // Replace with your password
            return dataSource;
        }

        @Bean
        @Qualifier("warehouseJdbcTemplate")
        public JdbcTemplate warehouseJdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }*/

    /*public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class); //Initialize the context
        JdbcTemplate warehouseJdbcTemplate = context.getBean("warehouseJdbcTemplate", JdbcTemplate.class); //Get the bean
        FactWorkTripDao factWorkTripDao = new FactWorkTripDao(warehouseJdbcTemplate); // Pass the jdbcTemplate

        // Example Usage:
        List<FactWorkTrip> top10Employees = factWorkTripDao.getTop10EmployeesByTrips();
        System.out.println("Top 10 Employees by Trips:");
        for (FactWorkTrip trip : top10Employees) {
            System.out.println(trip);  // Make sure FactWorkTrip has a proper toString() method
        }

        List<FactWorkTrip> top10Destinations = factWorkTripDao.getTop10Destinations();
        System.out.println("\nTop 10 Destinations:");
        for (FactWorkTrip trip : top10Destinations) {
            System.out.println(trip);
        }

        List<FactWorkTrip> totalCostByDept = factWorkTripDao.getTotalCostByDepartment();
        System.out.println("\nTotal Cost by Department:");
        for (FactWorkTrip trip : totalCostByDept) {
            System.out.println(trip);
        }

        List<FactWorkTrip> analysisByPurpose = factWorkTripDao.getAnalysisByPurpose();
        System.out.println("\nAnalysis by Purpose:");
        for (FactWorkTrip trip : analysisByPurpose) {
            System.out.println(trip);
        }

        List<FactWorkTrip> tripsByMonth = factWorkTripDao.getTripsByMonth();
        System.out.println("\nTrips by Month:");
        for (FactWorkTrip trip : tripsByMonth) {
            System.out.println(trip);
        }

        List<FactWorkTrip> costByDept = factWorkTripDao.getCostByDepartment();
        System.out.println("\nCost by Department:");
        for (FactWorkTrip trip : costByDept) {
            System.out.println(trip);
        }
        List<FactWorkTrip> costByPosition = factWorkTripDao.getCostByPosition();
        System.out.println("\nCost by Position:");
        for (FactWorkTrip trip : costByPosition) {
            System.out.println(trip);
        }

        context.close();
    }*/
}

