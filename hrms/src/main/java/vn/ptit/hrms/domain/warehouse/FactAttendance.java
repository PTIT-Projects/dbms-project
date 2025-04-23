package vn.ptit.hrms.domain.warehouse;

import vn.ptit.hrms.constant.AttendanceStatusEnum;

import java.time.LocalTime;

public class FactAttendance {
    private int attendanceSk;
    private int attendanceId;
    private int employeeSk;
    private String employeeName;
    private String departmentName;
    private String positionName;
    private int dateSk;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private double hoursWorked;
    private boolean isLate;
    private double overtimeHours;
    private boolean isEarlyLeave;
    private AttendanceStatusEnum status;

    public int getAttendanceSk() {
        return attendanceSk;
    }

    public void setAttendanceSk(int attendanceSk) {
        this.attendanceSk = attendanceSk;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getEmployeeSk() {
        return employeeSk;
    }

    public void setEmployeeSk(int employeeSk) {
        this.employeeSk = employeeSk;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getDateSk() {
        return dateSk;
    }

    public void setDateSk(int dateSk) {
        this.dateSk = dateSk;
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

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public boolean isEarlyLeave() {
        return isEarlyLeave;
    }

    public void setEarlyLeave(boolean earlyLeave) {
        isEarlyLeave = earlyLeave;
    }

    public AttendanceStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatusEnum status) {
        this.status = status;
    }
}
