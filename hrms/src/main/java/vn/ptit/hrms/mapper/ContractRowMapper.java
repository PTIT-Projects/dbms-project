package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.ContractStatusEnum;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.domain.Contract;
import vn.ptit.hrms.domain.Employee;

public class ContractRowMapper implements RowMapper<Contract> {
    private final EmployeeDao employeeDAO;

    public ContractRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contract contract = new Contract();

       
        contract.setId(rs.getInt("ContractID"));

       
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        contract.setEmployee(employee);

       
        contract.setContractType(rs.getString("ContractType"));

       
        Date startDate = rs.getDate("StartDate");
        if (startDate != null) {
            contract.setStartDate(startDate.toLocalDate());
        }

        Date endDate = rs.getDate("EndDate");
        if (endDate != null) {
            contract.setEndDate(endDate.toLocalDate());
        }

       
        contract.setSalary(rs.getDouble("Salary"));

       
        String statusStr = rs.getString("Status");
        if (statusStr != null) {
            contract.setStatus(getContractStatus(statusStr));
        }

        return contract;
    }

    private ContractStatusEnum getContractStatus(String value) {
        for (ContractStatusEnum status : ContractStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown contract status: " + value);
    }
}