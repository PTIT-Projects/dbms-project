package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.FactSalary;
import vn.ptit.hrms.constant.FactSalaryEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vn.ptit.hrms.mapper.warehouse.FactSalaryRowMapper;

@Repository
public class FactSalaryDao {

    private final JdbcTemplate jdbcTemplate;

    public FactSalaryDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2.a: Lương trung bình theo phòng ban và vị trí
    public List<FactSalary> getAverageSalaryByDepartmentAndPosition() {
        String sql = "SELECT " +
                "    department_name, " +
                "    position_name, " +
                "    COUNT(DISTINCT employee_name) AS employee_count, " +
                "    AVG(basic_salary) AS avg_basic_salary, " +
                "    AVG(allowance) AS avg_allowance, " +
                "    AVG(deductions) AS avg_deductions, " +
                "    AVG(total_salary) AS avg_total_salary " +
                "FROM fact_salary " +
                "WHERE payment_status = 'Đã thanh toán' " + // Chỉ xét các lần đã thanh toán
                "GROUP BY department_name, position_name " +
                "ORDER BY department_name, avg_total_salary DESC";
        return jdbcTemplate.query(sql, new FactSalaryRowMapper());
    }

    // 2.b: Lương cao nhất và thấp nhất
    // Top 10 nhân viên có tổng lương cao nhất
    public List<FactSalary> getTop10HighestSalary() {
        String sql = "SELECT  " +
                "    employee_name, " +
                "    department_name, " +
                "    position_name, " +
                "    total_salary, " +
                "    basic_salary, " +
                "    allowance, " +
                "    deductions " +
                "FROM fact_salary " +
                "WHERE payment_status = 'Đã thanh toán' " +
                "ORDER BY total_salary DESC " +
                "LIMIT 10"; // Changed TOP 10 to LIMIT 10 for cross-database compatibility
        return jdbcTemplate.query(sql, new FactSalaryRowMapper());
    }

    // Top 10 nhân viên có tổng lương thấp nhất
    public List<FactSalary> getTop10LowestSalary() {
        String sql = "SELECT  " +
                "    employee_name, " +
                "    department_name, " +
                "    position_name, " +
                "    total_salary, " +
                "    basic_salary, " +
                "    allowance, " +
                "    deductions " +
                "FROM fact_salary " +
                "WHERE payment_status = 'Đã thanh toán' " +
                "ORDER BY total_salary ASC " +
                "LIMIT 10";  // Changed TOP 10 to LIMIT 10
        return jdbcTemplate.query(sql, new FactSalaryRowMapper());
    }

    // 2.c: Tỷ lệ lương cơ bản/tổng lương
    // Theo phòng ban
    public List<FactSalary> getBasicSalaryRatioByDepartment() {
        String sql = "SELECT  " +
                "    department_name, " +
                "    AVG(basic_salary) AS avg_basic, " +
                "    AVG(total_salary) AS avg_total, " +
                "    ROUND(AVG(basic_salary * 100.0 / total_salary), 2) AS basic_salary_ratio, " +
                "    ROUND(AVG(allowance * 100.0 / total_salary), 2) AS allowance_ratio, " +
                "    ROUND(AVG(deductions * 100.0 / total_salary), 2) AS deductions_ratio " +
                "FROM fact_salary " +
                "WHERE total_salary > 0 AND payment_status = 'Đã thanh toán' " +
                "GROUP BY department_name " +
                "ORDER BY basic_salary_ratio DESC";
        return jdbcTemplate.query(sql, new FactSalaryRowMapper());
    }

    // Chi tiết theo từng nhân viên
    public List<FactSalary> getBasicSalaryRatioByEmployee() {
        String sql = "SELECT  " +
                "    employee_name, " +
                "    department_name, " +
                "    position_name, " +
                "    basic_salary, " +
                "    allowance, " +
                "    deductions, " +
                "    total_salary, " +
                "    ROUND(basic_salary * 100.0 / total_salary, 2) AS basic_salary_percentage, " +
                "    ROUND(allowance * 100.0 / total_salary, 2) AS allowance_percentage, " +
                "    ROUND(deductions * 100.0 / total_salary, 2) AS deductions_percentage " +
                "FROM fact_salary " +
                "WHERE total_salary > 0 AND payment_status = 'Đã thanh toán' " +
                "ORDER BY basic_salary_percentage DESC";
        return jdbcTemplate.query(sql, new FactSalaryRowMapper());
    }

    // 2.d: Xu hướng lương theo tháng năm 2025 (phân tích biến động lương)
    public List<FactSalary> getSalaryTrendByMonth2025() {
        String sql = "SELECT  " +
                "    dd.year, " +
                "    dd.month, " +
                "    AVG(fs.basic_salary) AS avg_basic, " +
                "    AVG(fs.total_salary) AS avg_total, " +
                "    SUM(fs.total_salary) AS department_total_payroll " +
                "FROM fact_salary fs " +
                "INNER JOIN dim_date dd ON fs.date_sk = dd.date_sk " +
                "WHERE dd.year = 2025 AND fs.payment_status = 'Đã thanh toán' " +
                "GROUP BY dd.year, dd.month, fs.department_name " +
                "ORDER BY dd.year, dd.month";
        return jdbcTemplate.query(sql, new FactSalaryRowMapper());
    }

    // 2.e: Phân tích lương theo độ tuổi và giới tính
    public List<FactSalary> getSalaryAnalysisByAgeAndGender() {
        String sql = "SELECT  " +
                "    de.gender, " +
                "    CASE  " +
                "        WHEN de.age < 25 THEN 'Dưới 25' " +
                "        WHEN de.age BETWEEN 25 AND 35 THEN '25-35' " +
                "        WHEN de.age BETWEEN 36 AND 45 THEN '36-45' " +
                "        WHEN de.age > 45 THEN 'Trên 45' " +
                "    END AS age_group, " +
                "    AVG(fs.total_salary) AS avg_salary, " +
                "    COUNT(*) AS employee_count " +
                "FROM fact_salary fs " +
                "INNER JOIN dim_employees de ON fs.employee_sk = de.employee_sk " +
                "WHERE fs.payment_status = 'Đã thanh toán' " +
                "GROUP BY  " +
                "    de.gender, " +
                "    CASE  " +
                "        WHEN de.age < 25 THEN 'Dưới 25' " +
                "        WHEN de.age BETWEEN 25 AND 35 THEN '25-35' " +
                "        WHEN de.age BETWEEN 36 AND 45 THEN '36-45' " +
                "        WHEN de.age > 45 THEN 'Trên 45' " +
                "    END " +
                "ORDER BY age_group, de.gender"; // Corrected the order by to include gender
        return jdbcTemplate.query(sql, new FactSalaryRowMapper());
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
        FactSalaryDao factSalaryDao = new FactSalaryDao(warehouseJdbcTemplate); // Pass the jdbcTemplate

        // Example Usage:
        List<FactSalary> avgSalaries = factSalaryDao.getAverageSalaryByDepartmentAndPosition();
        System.out.println("Average Salaries by Department and Position:");
        for (FactSalary salary : avgSalaries) {
            System.out.println(salary); // Ensure FactSalary has a proper toString()
        }

        List<FactSalary> top10Highest = factSalaryDao.getTop10HighestSalary();
        System.out.println("\nTop 10 Highest Salaries:");
        for (FactSalary salary : top10Highest) {
            System.out.println(salary);
        }

        List<FactSalary> top10Lowest = factSalaryDao.getTop10LowestSalary();
        System.out.println("\nTop 10 Lowest Salaries:");
        for (FactSalary salary : top10Lowest) {
            System.out.println(salary);
        }

        List<FactSalary> salaryRatios = factSalaryDao.getBasicSalaryRatioByDepartment();
        System.out.println("\nBasic Salary Ratios by Department:");
        for (FactSalary salary : salaryRatios) {
            System.out.println(salary);
        }
        List<FactSalary> salaryRatiosByEmployee = factSalaryDao.getBasicSalaryRatioByEmployee();
        System.out.println("\nBasic Salary Ratios by Employee:");
        for (FactSalary salary : salaryRatiosByEmployee) {
            System.out.println(salary);
        }

        List<FactSalary> salaryTrends = factSalaryDao.getSalaryTrendByMonth2025();
        System.out.println("\nSalary Trends in 2025:");
        for (FactSalary salary : salaryTrends) {
            System.out.println(salary);
        }

        List<FactSalary> salaryAnalysis = factSalaryDao.getSalaryAnalysisByAgeAndGender();
        System.out.println("\nSalary Analysis by Age and Gender:");
        for (FactSalary salary : salaryAnalysis) {
            System.out.println(salary);
        }

        context.close();
    }*/
}
