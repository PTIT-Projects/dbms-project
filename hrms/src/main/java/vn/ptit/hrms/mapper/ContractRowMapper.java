package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
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

        // Map Contract id
        contract.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        contract.setEmployee(employee);

        // Map contract type
        contract.setContractType(rs.getString("contract_type"));

        // Map LocalDate fields from SQL Date
        Date sqlStartDate = rs.getDate("start_date");
        if (sqlStartDate != null) {
            contract.setStartDate(sqlStartDate.toLocalDate());
        }

        Date sqlEndDate = rs.getDate("end_date");
        if (sqlEndDate != null) {
            contract.setEndDate(sqlEndDate.toLocalDate());
        }

        // Map salary
        contract.setSalary(rs.getDouble("salary"));

        // Map ContractStatusEnum field using a helper method.
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
}
