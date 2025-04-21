package vn.ptit.hrms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ptit.hrms.constant.AttendanceStatusEnum;
import vn.ptit.hrms.dao.primary.AttendanceDao;
import vn.ptit.hrms.domain.primary.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    public final AttendanceDao attendanceDao;

    public AttendanceService(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }

    public void createAttendance(Attendance attendance) {
        attendanceDao.createAttendance(attendance);
    }

    public Attendance getAttendanceById(Integer id) {
        return attendanceDao.getAttendanceById(id);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceDao.getAllAttendance();
    }

    public List<Attendance> getAttendanceByEmployee(Integer employeeId) {
        List<Attendance> allAttendance = attendanceDao.getAllAttendance();
        return allAttendance.stream()
                .filter(att -> att.getEmployee() != null &&
                        att.getEmployee().getId().equals(employeeId))
                .toList();
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        List<Attendance> allAttendance = attendanceDao.getAllAttendance();
        return allAttendance.stream()
                .filter(att -> att.getDate() != null && att.getDate().isEqual(date))
                .toList();
    }

    public List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Attendance> allAttendance = attendanceDao.getAllAttendance();
        return allAttendance.stream()
                .filter(att -> att.getDate() != null &&
                        !att.getDate().isBefore(startDate) &&
                        !att.getDate().isAfter(endDate))
                .toList();
    }

    public Map<AttendanceStatusEnum, Long> getAttendanceStatsByEmployee(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> employeeAttendance = getAttendanceByEmployee(employeeId);

        return employeeAttendance.stream()
                .filter(att -> att.getDate() != null &&
                        !att.getDate().isBefore(startDate) &&
                        !att.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(Attendance::getStatus, Collectors.counting()));
    }

    public void updateAttendance(Attendance attendance) {
        attendanceDao.updateAttendance(attendance);
    }

    public void deleteAttendance(Integer id) {
        attendanceDao.deleteAttendance(id);
    }
    public Page<Attendance> findAttendancePage(
            Pageable pageable,
            String employeeSearch,
            LocalDate startDate,
            LocalDate endDate,
            String status) {
        return attendanceDao.getAttendancePage(pageable, employeeSearch, startDate, endDate, status);
    }
}