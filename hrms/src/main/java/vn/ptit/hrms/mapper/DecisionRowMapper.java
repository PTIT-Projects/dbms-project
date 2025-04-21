package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.DecisionTypeEnum;
import vn.ptit.hrms.dao.primary.EmployeeDao;
import vn.ptit.hrms.domain.primary.Decision;
import vn.ptit.hrms.domain.primary.Employee;

public class DecisionRowMapper implements RowMapper<Decision> {

    private final EmployeeDao employeeDAO;

    public DecisionRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Decision mapRow(ResultSet rs, int rowNum) throws SQLException {
        Decision decision = new Decision();

       
        decision.setId(rs.getInt("DecisionID"));

       
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        decision.setEmployee(employee);

       
        String decisionTypeValue = rs.getNString("DecisionType");
        if (decisionTypeValue != null) {
            decision.setDecisionType(getDecisionType(decisionTypeValue));
        }

       
        Date sqlDecisionDate = rs.getDate("DecisionDate");
        if (sqlDecisionDate != null) {
            decision.setDecisionDate(sqlDecisionDate.toLocalDate());
        }

       
        decision.setDetails(rs.getNString("Details"));

        return decision;
    }


    private DecisionTypeEnum getDecisionType(String value) {
        for (DecisionTypeEnum type : DecisionTypeEnum.values()) {
           
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown decision type value: " + value);
    }
}