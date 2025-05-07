package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class EmployeeOvertimeDTO {
    private String employeeName;
    private String departmentName;
    private BigDecimal totalOvertime;
    private BigDecimal avgOvertimePerDay;

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public BigDecimal getTotalOvertime() { return totalOvertime; }
    public void setTotalOvertime(BigDecimal totalOvertime) { this.totalOvertime = totalOvertime; }
    public BigDecimal getAvgOvertimePerDay() { return avgOvertimePerDay; }
    public void setAvgOvertimePerDay(BigDecimal avgOvertimePerDay) { this.avgOvertimePerDay = avgOvertimePerDay; }
}