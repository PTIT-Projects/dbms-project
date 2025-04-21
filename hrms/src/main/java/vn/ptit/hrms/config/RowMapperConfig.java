package vn.ptit.hrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ptit.hrms.dao.primary.DepartmentDao;
import vn.ptit.hrms.dao.primary.EmployeeDao;
import vn.ptit.hrms.dao.primary.PositionDao;
import vn.ptit.hrms.dao.primary.RecruitmentPlanDao;
import vn.ptit.hrms.mapper.*;

@Configuration
public class RowMapperConfig {

    @Bean
    public ContractRowMapper contractRowMapper(EmployeeDao employeeDao) {
        return new ContractRowMapper(employeeDao);
    }

    @Bean
    public AttendanceRowMapper attendanceRowMapper(EmployeeDao employeeDao) {
        return new AttendanceRowMapper(employeeDao);
    }

    @Bean
    public DecisionRowMapper decisionRowMapper(EmployeeDao employeeDao) {
        return new DecisionRowMapper(employeeDao);
    }


    @Bean
    public EmployeeRowMapper employeeRowMapper(PositionDao positionDao, DepartmentDao departmentDao) {
        return new EmployeeRowMapper(positionDao, departmentDao);
    }

    @Bean
    public InsuranceRowMapper insuranceRowMapper(EmployeeDao employeeDao) {
        return new InsuranceRowMapper(employeeDao);
    }

    @Bean
    public LeaveBalanceRowMapper leaveBalanceRowMapper(EmployeeDao employeeDao) {
        return new LeaveBalanceRowMapper(employeeDao);
    }

    @Bean
    public RecruitmentPlanRowMapper recruitmentPlanRowMapper(DepartmentDao departmentDao, PositionDao positionDao) {
        return new RecruitmentPlanRowMapper(departmentDao, positionDao);
    }
    @Bean
    public DepartmentManagerRowMapper departmentManagerRowMapper(DepartmentDao departmentDao, EmployeeDao employeeDao) {
        return new DepartmentManagerRowMapper(departmentDao, employeeDao);
    }




    @Bean
    public WorkTripRequestRowMapper workTripRequestRowMapper(EmployeeDao employeeDao) {
        return new WorkTripRequestRowMapper(employeeDao);
    }

    @Bean
    public SalaryRowMapper salaryRowMapper(EmployeeDao employeeDao) {
        return new SalaryRowMapper(employeeDao);
    }

    @Bean
    public RegistrationRowMapper registrationRowMapper(EmployeeDao employeeDao) {
        return new RegistrationRowMapper(employeeDao);
    }


    @Bean
    public DepartmentRowMapper departmentRowMapper() {
        return new DepartmentRowMapper();
    }

    @Bean
    public PositionRowMapper positionRowMapper(DepartmentDao departmentDao) {
        return new PositionRowMapper(departmentDao);
    }

    @Bean
    public NotificationRowMapper notificationRowMapper(EmployeeDao employeeDao) {
        return new NotificationRowMapper(employeeDao);
    }

    @Bean
    public ApplicantRowMapper applicantRowMapper(RecruitmentPlanDao recruitmentPlanDao) {
        return new ApplicantRowMapper(recruitmentPlanDao);
    }


}