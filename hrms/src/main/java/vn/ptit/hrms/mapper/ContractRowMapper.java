package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.ContractStatusEnum;
import vn.ptit.hrms.domain.Contract;
import vn.ptit.hrms.domain.Employee;

public class ContractRowMapper implements RowMapper<Contract> {

    @Override
    public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contract contract = new Contract();

        // Map Contract id
        contract.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using a method
        int employeeId = rs.getInt("employee_id");
        Employee employee = findEmployeeById(employeeId); // Implement this method as needed
        contract.setEmployee(employee);

        // Map contract type
        contract.setContractType(rs.getString("contract_type"));

        // Map start date
        Date sqlStartDate = rs.getDate("start_date");
        if (sqlStartDate != null) {
            contract.setStartDate(sqlStartDate.toLocalDate());
        }

        // Map end date
        Date sqlEndDate = rs.getDate("end_date");
        if (sqlEndDate != null) {
            contract.setEndDate(sqlEndDate.toLocalDate());
        }

        // Map salary
        contract.setSalary(rs.getDouble("salary"));

        // Map ContractStatusEnum
        String statusValue = rs.getString("status");
        if (statusValue != null) {
            contract.setStatus(getContractStatus(statusValue));
        }

        return contract;
    }

    /**
     * Converts a string value from the database into the corresponding ContractStatusEnum.
     */
    private ContractStatusEnum getContractStatus(String value) {
        for (ContractStatusEnum status : ContractStatusEnum.values()) {
            // Adjust the comparison if your enum uses a custom value
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown contract status value: " + value);
    }

    // Placeholder for a method to retrieve Employee by ID
    private Employee findEmployeeById(int employeeId) {
        // Implement the logic to retrieve an Employee object by its ID
        // This could involve calling an EmployeeDAO or similar service
        return null; // Replace with actual implementation
    }
}
