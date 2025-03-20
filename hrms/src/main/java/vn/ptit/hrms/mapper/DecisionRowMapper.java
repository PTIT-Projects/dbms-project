package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.DecisionTypeEnum;
import vn.ptit.hrms.dao.EmployeeDAO;
import vn.ptit.hrms.domain.Decision;
import vn.ptit.hrms.domain.Employee;

public class DecisionRowMapper implements RowMapper<Decision> {

    private final EmployeeDAO employeeDAO;

    public DecisionRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Decision mapRow(ResultSet rs, int rowNum) throws SQLException {
        Decision decision = new Decision();

        // Map Decision id
        decision.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        Employee employee = employeeDAO.findById(employeeId);
        decision.setEmployee(employee);

        // Map DecisionTypeEnum field using a helper method.
        String decisionTypeValue = rs.getString("decision_type");
        if (decisionTypeValue != null) {
            decision.setDecisionType(getDecisionType(decisionTypeValue));
        }

        // Map decision date
        Date sqlDecisionDate = rs.getDate("decision_date");
        if (sqlDecisionDate != null) {
            decision.setDecisionDate(sqlDecisionDate.toLocalDate());
        }

        // Map details
        decision.setDetails(rs.getString("details"));

        return decision;
    }

    /**
     * Converts a string value from the database into the corresponding DecisionTypeEnum.
     */
    private DecisionTypeEnum getDecisionType(String value) {
        for (DecisionTypeEnum type : DecisionTypeEnum.values()) {
            // Adjust the comparison if your enum uses a custom value
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown decision type value: " + value);
    }
}
