package vn.ptit.hrms.domain.warehouse;

import java.time.LocalDate;
import java.time.LocalTime;

public class FactAttendance {
    private Integer attendanceKey;
    private Integer employeeKey;
    private Integer timeKey;
    private Integer departmentKey;
    private Integer attendanceId;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String attendanceStatus;
    private Double hoursWorked;
    private Boolean isLate;
    private Boolean isAbsent;
    private LocalDate workDate;

    public Integer getAttendanceKey() {
        return attendanceKey;
    }

    public void setAttendanceKey(Integer attendanceKey) {
        this.attendanceKey = attendanceKey;
    }

    public Integer getEmployeeKey() {
        return employeeKey;
    }

    public void setEmployeeKey(Integer employeeKey) {
        this.employeeKey = employeeKey;
    }

    public Integer getTimeKey() {
        return timeKey;
    }

    public void setTimeKey(Integer timeKey) {
        this.timeKey = timeKey;
    }

    public Integer getDepartmentKey() {
        return departmentKey;
    }

    public void setDepartmentKey(Integer departmentKey) {
        this.departmentKey = departmentKey;
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public Boolean getLate() {
        return isLate;
    }

    public void setLate(Boolean late) {
        isLate = late;
    }

    public Boolean getAbsent() {
        return isAbsent;
    }

    public void setAbsent(Boolean absent) {
        isAbsent = absent;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    // Getters and setters
}