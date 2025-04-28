package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.dto.demographic.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FactDemographicAnalysisDao {
    private final JdbcTemplate jdbcTemplate;

    public FactDemographicAnalysisDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 9.a Phân tích lương theo giới tính và theo độ tuổi
    public List<SalaryByAgeGenderGroupDTO> getSalaryByAgeGenderGroups() {
        String sql = """
            WITH employee_age_groups AS (
                SELECT 
                    employee_sk,
                    gender,
                    CASE 
                        WHEN age BETWEEN 18 AND 25 THEN '18-25'
                        WHEN age BETWEEN 26 AND 35 THEN '26-35'
                        WHEN age BETWEEN 36 AND 45 THEN '36-45'
                        WHEN age BETWEEN 46 AND 55 THEN '46-55'
                        ELSE '56+'
                    END AS age_group
                FROM 
                    dim_employees
            )
            SELECT 
                eag.gender,
                eag.age_group,
                ROUND(AVG(fs.total_salary), 2) AS avg_salary
            FROM 
                fact_salary fs
            JOIN 
                employee_age_groups eag ON fs.employee_sk = eag.employee_sk
            GROUP BY 
                eag.gender, eag.age_group
            ORDER BY 
                eag.gender, eag.age_group
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<SalaryByAgeGenderGroupDTO>() {
            @Override
            public SalaryByAgeGenderGroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalaryByAgeGenderGroupDTO dto = new SalaryByAgeGenderGroupDTO();
                dto.setGender(rs.getString("gender"));
                dto.setAgeGroup(rs.getString("age_group"));
                dto.setAvgSalary(rs.getBigDecimal("avg_salary"));
                return dto;
            }
        });
    }

    // 9.b Số giờ làm việc trung bình theo nhóm tuổi
    public List<WorkHoursByAgeGroupDTO> getWorkHoursByAgeGroup() {
        String sql = """
            SELECT 
                CASE 
                    WHEN age BETWEEN 18 AND 25 THEN '18-25'
                    WHEN age BETWEEN 26 AND 35 THEN '26-35'
                    WHEN age BETWEEN 36 AND 45 THEN '36-45'
                    ELSE '46+'
                END AS age_group,
                ROUND(AVG(fa.hours_worked), 2) AS avg_hours_worked
            FROM 
                fact_attendance fa
            JOIN 
                dim_employees e ON fa.employee_sk = e.employee_sk
            GROUP BY 
                CASE 
                    WHEN age BETWEEN 18 AND 25 THEN '18-25'
                    WHEN age BETWEEN 26 AND 35 THEN '26-35'
                    WHEN age BETWEEN 36 AND 45 THEN '36-45'
                    ELSE '46+'
                END
            ORDER BY 
                avg_hours_worked DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<WorkHoursByAgeGroupDTO>() {
            @Override
            public WorkHoursByAgeGroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                WorkHoursByAgeGroupDTO dto = new WorkHoursByAgeGroupDTO();
                dto.setAgeGroup(rs.getString("age_group"));
                dto.setAvgHoursWorked(rs.getDouble("avg_hours_worked"));
                return dto;
            }
        });
    }

    // 9.c Số ngày nghỉ phép trung bình theo giới tính
    public List<LeaveByGenderDTO> getAverageLeaveByGender() {
        String sql = """
            SELECT 
                e.gender,
                ROUND(AVG(flb.used_leave_days), 2) AS avg_leave_days
            FROM 
                fact_leave_balance flb
            JOIN 
                dim_employees e ON flb.employee_sk = e.employee_sk
            GROUP BY 
                e.gender
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<LeaveByGenderDTO>() {
            @Override
            public LeaveByGenderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                LeaveByGenderDTO dto = new LeaveByGenderDTO();
                dto.setGender(rs.getString("gender"));
                dto.setAvgLeaveDays(rs.getDouble("avg_leave_days"));
                return dto;
            }
        });
    }

    // 9.d Phân bổ nhân viên theo độ tuổi và phòng ban
    public List<EmployeeDistributionDTO> getEmployeeDistributionByAgeAndDepartment() {
        String sql = """
            SELECT 
                e.department_name,
                CASE 
                    WHEN e.age BETWEEN 18 AND 30 THEN '18-30'
                    WHEN e.age BETWEEN 31 AND 45 THEN '31-45'
                    ELSE '46+'
                END AS age_group,
                COUNT(*) AS employee_count
            FROM 
                dim_employees e
            GROUP BY 
                e.department_name,
                CASE 
                    WHEN e.age BETWEEN 18 AND 30 THEN '18-30'
                    WHEN e.age BETWEEN 31 AND 45 THEN '31-45'
                    ELSE '46+'
                END
            ORDER BY 
                e.department_name,
                age_group
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<EmployeeDistributionDTO>() {
            @Override
            public EmployeeDistributionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                EmployeeDistributionDTO dto = new EmployeeDistributionDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setAgeGroup(rs.getString("age_group"));
                dto.setEmployeeCount(rs.getInt("employee_count"));
                return dto;
            }
        });
    }

    // 9.e Tỷ lệ khen thưởng theo giới tính
    public List<RewardsByGenderDTO> getRewardPercentageByGender() {
        String sql = """
            SELECT 
                e.gender,
                COUNT(fd.decision_sk) AS total_rewards,
                ROUND(COUNT(fd.decision_sk) * 100.0 / SUM(COUNT(fd.decision_sk)) OVER (), 2) AS reward_percentage
            FROM 
                fact_decision fd
            JOIN 
                dim_employees e ON fd.employee_sk = e.employee_sk
            WHERE 
                fd.decision_type = 'Khen thưởng'
            GROUP BY 
                e.gender
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<RewardsByGenderDTO>() {
            @Override
            public RewardsByGenderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RewardsByGenderDTO dto = new RewardsByGenderDTO();
                dto.setGender(rs.getString("gender"));
                dto.setTotalRewards(rs.getInt("total_rewards"));
                dto.setRewardPercentage(rs.getDouble("reward_percentage"));
                return dto;
            }
        });
    }

    // 9.f Phân tích giờ làm thêm theo giới tính và vị trí
    public List<OvertimeByGenderPositionDTO> getOvertimeByGenderAndPosition() {
        String sql = """
            SELECT 
                e.gender,
                e.position_name,
                SUM(fr.registration_duration) AS total_overtime_hours
            FROM 
                fact_registrations fr
            JOIN 
                dim_employees e ON fr.employee_sk = e.employee_sk
            WHERE 
                fr.registration_type = 'Làm thêm'
            GROUP BY 
                e.gender, e.position_name
            ORDER BY 
                total_overtime_hours DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<OvertimeByGenderPositionDTO>() {
            @Override
            public OvertimeByGenderPositionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                OvertimeByGenderPositionDTO dto = new OvertimeByGenderPositionDTO();
                dto.setGender(rs.getString("gender"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setTotalOvertimeHours(rs.getDouble("total_overtime_hours"));
                return dto;
            }
        });
    }

    // 9.g Tuổi trung bình của nhân viên theo phòng ban
    public List<AverageAgeByDepartmentDTO> getAverageAgeByDepartment() {
        String sql = """
            SELECT 
                e.department_name,
                ROUND(AVG(e.age), 0) AS avg_age
            FROM 
                dim_employees e
            GROUP BY 
                e.department_name
            ORDER BY 
                avg_age DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<AverageAgeByDepartmentDTO>() {
            @Override
            public AverageAgeByDepartmentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                AverageAgeByDepartmentDTO dto = new AverageAgeByDepartmentDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setAvgAge(rs.getInt("avg_age"));
                return dto;
            }
        });
    }

    // 9.h Tỷ lệ nghỉ việc theo nhóm tuổi
    public List<ResignationRateByAgeGroupDTO> getResignationRateByAgeGroup() {
        String sql = """
            SELECT
                CASE
                    WHEN age BETWEEN 18 AND 30 THEN '18-30'
                    WHEN age BETWEEN 31 AND 45 THEN '31-45'
                    ELSE '46+'
                END AS age_group,
                COUNT(*) AS total_employees,
                SUM(CASE WHEN contract_end_date < GETDATE() THEN 1 ELSE 0 END) AS resigned_count,  
                ROUND(SUM(CASE WHEN contract_end_date < GETDATE() THEN 1.0 ELSE 0 END) * 100.0 / COUNT(*), 2) AS resignation_rate 
            FROM
                dim_employees
            GROUP BY    
                CASE
                    WHEN age BETWEEN 18 AND 30 THEN '18-30'
                    WHEN age BETWEEN 31 AND 45 THEN '31-45'
                    ELSE '46+'
                END
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<ResignationRateByAgeGroupDTO>() {
            @Override
            public ResignationRateByAgeGroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ResignationRateByAgeGroupDTO dto = new ResignationRateByAgeGroupDTO();
                dto.setAgeGroup(rs.getString("age_group"));
                dto.setTotalEmployees(rs.getInt("total_employees"));
                dto.setResignedCount(rs.getInt("resigned_count"));
                dto.setResignationRate(rs.getDouble("resignation_rate"));
                return dto;
            }
        });
    }
}