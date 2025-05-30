package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.primary.Department;
import vn.ptit.hrms.domain.primary.Position;
import vn.ptit.hrms.dao.primary.DepartmentDao;

public class PositionRowMapper implements RowMapper<Position> {

    private final DepartmentDao departmentDAO;

    public PositionRowMapper(DepartmentDao departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @Override
    public Position mapRow(ResultSet rs, int rowNum) throws SQLException {
        Position position = new Position();

       
        position.setId(rs.getInt("PositionID"));

       
        position.setPositionName(rs.getNString("PositionName"));

       
        int departmentId = rs.getInt("DepartmentID");
        if (departmentId > 0) {
            Department department = departmentDAO.getDepartmentById(departmentId);
            position.setDepartment(department);
        }

        return position;
    }
}