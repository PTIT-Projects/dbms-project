package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.AttendanceFactDao;
import vn.ptit.hrms.dao.warehouse.EmployeeDimensionDao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {
    private final AttendanceFactDao attendanceFactDao;
    private final EmployeeDimensionDao employeeDimensionDao;

    public AnalyticsService(
            AttendanceFactDao attendanceFactDao,
            EmployeeDimensionDao employeeDimensionDao
            ) {
        this.attendanceFactDao = attendanceFactDao;
        this.employeeDimensionDao = employeeDimensionDao;
    }

    public Map<String, Double> getAverageAttendanceByDepartment(int year, int month) {
        List<Map<String, Object>> results = attendanceFactDao.getAverageHoursByDepartment(year, month);
        System.out.println("Query results: " + results);
        Map<String, Double> departmentHours = new HashMap<>();

        for (Map<String, Object> result : results) {
            String department = (String) result.get("department_name");
            Double avgHours = ((Number) result.get("avg_hours")).doubleValue();
            departmentHours.put(department, avgHours);
        }

        return departmentHours;
    }

    public Map<String, Integer> getAttendanceStatusDistribution(int year, int month) {
        List<Map<String, Object>> results = attendanceFactDao.getAttendanceStatusDistribution(year, month);
        Map<String, Integer> statusDistribution = new HashMap<>();

        for (Map<String, Object> result : results) {
            String status = (String) result.get("attendance_status");
            Integer count = ((Number) result.get("count")).intValue();
            statusDistribution.put(status, count);
        }

        return statusDistribution;
    }

    // Add more analytics methods as needed
}