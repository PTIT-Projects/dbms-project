package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.AttendanceStatusEnum;
import vn.ptit.hrms.domain.warehouse.FactAttendance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class FactAttendanceRowMapper implements RowMapper<FactAttendance> {
    @Override
    public FactAttendance mapRow(ResultSet rs, int rowNum) throws SQLException {
        FactAttendance factAttendance = new FactAttendance();
        factAttendance.setAttendanceSk(rs.getInt("attendance_sk"));
        factAttendance.setAttendanceId(rs.getInt("attendance_id"));
        factAttendance.setEmployeeSk(rs.getInt("employee_sk"));
        factAttendance.setEmployeeName(rs.getNString("employee_name"));
        factAttendance.setDepartmentName(rs.getNString("department_name"));
        factAttendance.setPositionName(rs.getNString("position_name"));
        factAttendance.setDateSk(rs.getInt("date_sk"));
        factAttendance.setCheckInTime(rs.getObject("check_in_time", LocalTime.class));
        factAttendance.setCheckOutTime(rs.getObject("check_out_time", LocalTime.class));
        factAttendance.setHoursWorked(rs.getDouble("hours_worked"));
        factAttendance.setLate(rs.getBoolean("is_late"));
        factAttendance.setOvertimeHours(rs.getDouble("overtime_hours"));
        factAttendance.setEarlyLeave(rs.getBoolean("is_early_leave"));
        factAttendance.setStatus(this.getAttendanceStatus(rs.getNString("status")));
        return factAttendance;
    }
    private AttendanceStatusEnum getAttendanceStatus(String value) {
        for (AttendanceStatusEnum status : AttendanceStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status value: " + value);
    }
}
