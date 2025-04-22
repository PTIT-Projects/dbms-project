package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.FactWorkTrip;
import vn.ptit.hrms.constant.FactWorkTripStatusEnum; // Import the enum

import java.sql.ResultSet;
import java.sql.SQLException;

public class FactWorkTripRowMapper implements RowMapper<FactWorkTrip> {

    @Override
    public FactWorkTrip mapRow(ResultSet rs, int rowNum) throws SQLException {
        FactWorkTrip workTrip = new FactWorkTrip();
        workTrip.setWorkTripSk(rs.getInt("work_trip_sk"));
        workTrip.setWorkTripId(rs.getInt("work_trip_id"));
        workTrip.setEmployeeSk(rs.getInt("employee_sk"));
        workTrip.setEmployeeName(rs.getString("employee_name"));
        workTrip.setDepartmentName(rs.getString("department_name"));
        workTrip.setPositionName(rs.getString("position_name"));
        workTrip.setStartDateSk(rs.getInt("start_date_sk"));
        workTrip.setEndDateSk(rs.getInt("end_date_sk"));
        workTrip.setTripDuration(rs.getInt("trip_duration"));
        workTrip.setDestination(rs.getString("destination"));
        workTrip.setPurpose(rs.getString("purpose"));
        workTrip.setTotalCost(rs.getDouble("total_cost"));

        // Convert from String to Enum
        String statusString = rs.getString("status");
        FactWorkTripStatusEnum status = null;
        if (statusString != null) {
            try {
                status = FactWorkTripStatusEnum.valueOf(statusString);
            } catch (IllegalArgumentException e) {
                // Handle the case where the string doesn't match any enum value
                // You might want to log an error or set a default value
                status = FactWorkTripStatusEnum.CANCELED; // Or set a default enum value
            }
        }
        workTrip.setStatus(status);
        return workTrip;
    }
}

