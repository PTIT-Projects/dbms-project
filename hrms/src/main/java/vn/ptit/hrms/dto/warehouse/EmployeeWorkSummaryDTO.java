package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class EmployeeWorkSummaryDTO {
    private String employeeName;
    private String departmentName;
    private BigDecimal totalHoursWorked;
    private int workingDays;

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public BigDecimal getTotalHoursWorked() { return totalHoursWorked; }
    public void setTotalHoursWorked(BigDecimal totalHoursWorked) { this.totalHoursWorked = totalHoursWorked; }
    public int getWorkingDays() { return workingDays; }
    public void setWorkingDays(int workingDays) { this.workingDays = workingDays; }
}