package vn.ptit.hrms.domain;

import vn.ptit.hrms.constant.AttendanceStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private Integer id;
    private Employee employee;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private AttendanceStatus status;
}
