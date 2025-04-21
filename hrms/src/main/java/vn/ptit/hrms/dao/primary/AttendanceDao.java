package vn.ptit.hrms.dao.primary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.primary.Attendance;
import vn.ptit.hrms.mapper.AttendanceRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttendanceDao {
    private final JdbcTemplate jdbcTemplate;
    private final AttendanceRowMapper attendanceRowMapper;

    public AttendanceDao(JdbcTemplate jdbcTemplate, AttendanceRowMapper attendanceRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.attendanceRowMapper = attendanceRowMapper;
    }

    public void createAttendance(Attendance attendance) {
        String sql = "INSERT INTO Attendance (EmployeeID, Date, CheckInTime, CheckOutTime, Status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                attendance.getEmployee().getId(),
                attendance.getDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getStatus().getValue());
    }

    public Attendance getAttendanceById(Integer id) {
        String sql = "SELECT * FROM Attendance WHERE AttendanceID = ?";
        return jdbcTemplate.queryForObject(sql, attendanceRowMapper, id);
    }

    public List<Attendance> getAllAttendance() {
        String sql = "SELECT * FROM Attendance";
        return jdbcTemplate.query(sql, attendanceRowMapper);
    }

    public void updateAttendance(Attendance attendance) {
        String sql = "UPDATE Attendance SET EmployeeID = ?, Date = ?, CheckInTime = ?, CheckOutTime = ?, Status = ? WHERE AttendanceID = ?";
        jdbcTemplate.update(sql,
                attendance.getEmployee().getId(),
                attendance.getDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getStatus().getValue(),
                attendance.getId());
    }

    public void deleteAttendance(Integer id) {
        String sql = "DELETE FROM Attendance WHERE AttendanceID = ?";
        jdbcTemplate.update(sql, id);
    }

    public Page<Attendance> getAttendancePage(
            Pageable pageable,
            String employeeSearch,
            LocalDate startDate,
            LocalDate endDate,
            String status) {

        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM Attendance a LEFT JOIN Employees e ON a.EmployeeID = e.EmployeeID");
        StringBuilder dataSql = new StringBuilder("SELECT a.*, e.* FROM Attendance a LEFT JOIN Employees e ON a.EmployeeID = e.EmployeeID");
        List<Object> params = new ArrayList<>();
        if ((startDate != null && endDate != null)|| (employeeSearch != null && !employeeSearch.isEmpty()) || (status != null && !status.isEmpty())) {
            String whereClause = buildWhereClause(employeeSearch, startDate, endDate, status, params);
            countSql.append(whereClause);
            dataSql.append(whereClause);
        }
        Integer total = jdbcTemplate.queryForObject(countSql.toString(), Integer.class, params.toArray());
        dataSql.append(" ORDER BY a.Date DESC, a.CheckInTime OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(pageable.getOffset());
        params.add(pageable.getPageSize());
        List<Attendance> attendances = jdbcTemplate.query(dataSql.toString(), attendanceRowMapper, params.toArray());

        return new PageImpl<>(attendances, pageable, total != null ? total : 0);
    }
    public String buildWhereClause(String employeeSearch, LocalDate startDate, LocalDate endDate, String status, List<Object> params) {
        StringBuilder whereClause = new StringBuilder(" WHERE ");
        boolean whereAdded = false;

        if (employeeSearch != null && !employeeSearch.isEmpty()) {
            whereClause.append(" e.FullName LIKE ?");
            params.add("%" + employeeSearch + "%");
            whereAdded = true;
        }

        if (startDate != null && endDate != null) {
            if (whereAdded) {
                whereClause.append(" AND a.Date BETWEEN ? AND ?");
            } else {
                whereClause.append(" a.Date BETWEEN ? AND ?");
                whereAdded = true;
            }
            params.add(startDate);
            params.add(endDate);
        }

        if (status != null && !status.isEmpty()) {
            if (whereAdded) {
                whereClause.append(" AND a.Status = ?");
            } else {
                whereClause.append(" a.Status = ?");
            }
            params.add(status);
        }
        return whereClause.toString();
    }
}