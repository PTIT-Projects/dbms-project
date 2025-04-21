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
}
