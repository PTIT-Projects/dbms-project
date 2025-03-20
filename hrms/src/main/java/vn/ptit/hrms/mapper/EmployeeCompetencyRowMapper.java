package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.EmployeeCompetencyLevelEnum;
import vn.ptit.hrms.domain.Competency;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.EmployeeCompetency;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.dao.CompetencyDao;

public class EmployeeCompetencyRowMapper implements RowMapper<EmployeeCompetency> {

    private final EmployeeDao employeeDAO;
    private final CompetencyDao competencyDAO;

    public EmployeeCompetencyRowMapper(EmployeeDao employeeDAO, CompetencyDao competencyDAO) {
        this.employeeDAO = employeeDAO;
        this.competencyDAO = competencyDAO;
    }

    @Override
    public EmployeeCompetency mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeCompetency employeeCompetency = new EmployeeCompetency();

        // Map EmployeeCompetency id - use the correct column name EmployeeCompetencyID
        employeeCompetency.setId(rs.getInt("EmployeeCompetencyID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        employeeCompetency.setEmployee(employee);

        // Retrieve competency id from ResultSet and fetch the full Competency using CompetencyDAO.
        int competencyId = rs.getInt("CompetencyID");
        Competency competency = competencyDAO.getCompetencyById(competencyId);
        employeeCompetency.setCompetency(competency);

        // Map EmployeeCompetencyLevelEnum field - use the correct column name Level
        String levelValue = rs.getString("Level");
        if (levelValue != null) {
            employeeCompetency.setLevel(getEmployeeCompetencyLevelEnum(levelValue));
        }

        return employeeCompetency;
    }

    /**
     * Converts a string value from the database into the corresponding EmployeeCompetencyLevelEnum.
     */
    private EmployeeCompetencyLevelEnum getEmployeeCompetencyLevelEnum(String value) {
        for (EmployeeCompetencyLevelEnum level : EmployeeCompetencyLevelEnum.values()) {
            if (level.getValue().equalsIgnoreCase(value)) {  // Use getValue() instead of name()
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown competency level value: " + value);
    }
}