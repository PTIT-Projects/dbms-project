package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.DimPosition;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DimPositionsRowMapper implements RowMapper<DimPosition> {
    @Override
    public DimPosition mapRow(ResultSet rs, int rowNum) throws SQLException {
        DimPosition position = new DimPosition();
        position.setPositionSk(rs.getInt("position_sk"));
        position.setPositionId(rs.getInt("position_id"));
        position.setPositionName(rs.getString("position_name"));
        position.setDepartmentSk(rs.getInt("department_sk"));
        position.setDepartmentName(rs.getString("department_name"));
        return position;
    }
}

