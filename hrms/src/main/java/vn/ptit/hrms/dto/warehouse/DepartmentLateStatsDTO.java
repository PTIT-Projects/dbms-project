package vn.ptit.hrms.dto.warehouse;

public class DepartmentLateStatsDTO {
    private String departmentName;
    private int totalDays;
    private int lateDays;
    private double latePercentage;

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }
    public int getLateDays() { return lateDays; }
    public void setLateDays(int lateDays) { this.lateDays = lateDays; }
    public double getLatePercentage() { return latePercentage; }
    public void setLatePercentage(double latePercentage) { this.latePercentage = latePercentage; }
}