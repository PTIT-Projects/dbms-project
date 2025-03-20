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

       
        employeeCompetency.setId(rs.getInt("EmployeeCompetencyID"));

       
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        employeeCompetency.setEmployee(employee);

       
        int competencyId = rs.getInt("CompetencyID");
        Competency competency = competencyDAO.getCompetencyById(competencyId);
        employeeCompetency.setCompetency(competency);

       
        String levelValue = rs.getString("Level");
        if (levelValue != null) {
            employeeCompetency.setLevel(getEmployeeCompetencyLevelEnum(levelValue));
        }

        return employeeCompetency;
    }

    private EmployeeCompetencyLevelEnum getEmployeeCompetencyLevelEnum(String value) {
        for (EmployeeCompetencyLevelEnum level : EmployeeCompetencyLevelEnum.values()) {
            if (level.getValue().equalsIgnoreCase(value)) { 
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown competency level value: " + value);
    }
}