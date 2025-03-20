package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Department;
import vn.ptit.hrms.domain.Position;
import vn.ptit.hrms.dao.DepartmentDao; // Assuming you have a DepartmentDAO to fetch Department

public class PositionRowMapper implements RowMapper<Position> {

    private final DepartmentDao departmentDAO;

    public PositionRowMapper(DepartmentDao departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @Override
    public Position mapRow(ResultSet rs, int rowNum) throws SQLException {
        Position position = new Position();

        // Map Position id
        position.setId(rs.getInt("position_id")); // Assuming the column name is position_id

        // Map Position name
        position.setPositionName(rs.getString("position_name")); // Assuming the column name is position_name

        // Retrieve department id from ResultSet and fetch the full Department using DepartmentDAO.
        int departmentId = rs.getInt("department_id"); // Assuming the column name is department_id
        if (departmentId > 0) { // Assuming department ID is positive
            Department department = departmentDAO.getDepartmentById(departmentId);
            position.setDepartment(department);
        }

        return position;
    }
}
