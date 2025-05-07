package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.DecisionTypeEnum;
import vn.ptit.hrms.domain.warehouse.FactDecision;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FactDecisionRowMapper implements RowMapper<FactDecision> {
    @Override
    public FactDecision mapRow(ResultSet rs, int rowNum) throws SQLException {
        FactDecision factDecision = new FactDecision();
        factDecision.setDecisionSk(rs.getInt("decision_sk"));
        factDecision.setDecisionId(rs.getInt("decision_id"));
        factDecision.setEmployeeSk(rs.getInt("employee_sk"));
        factDecision.setEmployeeName(rs.getNString("employee_name"));
        factDecision.setDepartmentName(rs.getNString("department_name"));
        factDecision.setDecisionDateSk(rs.getInt("decision_date_sk"));
        factDecision.setDecisionType(this.getDecisionType(rs.getNString("decision_type")));
        factDecision.setDecisionDetail(rs.getNString("decision_details"));
        factDecision.setDecisionEffectiveDate(rs.getObject("decision_effective_date", LocalDate.class));
        factDecision.setDecisionExpiryDate(rs.getObject("decision_expiry_date", LocalDate.class));
        return factDecision;
    }
    private DecisionTypeEnum getDecisionType(String value) {
        for (DecisionTypeEnum status : DecisionTypeEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status value: " + value);
    }
}
