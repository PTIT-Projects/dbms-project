package vn.ptit.hrms.dto.warehouse;

public class LeaveTypeStatsDTO {
    private String leaveType;
    private int totalDaysUsed;
    private int totalEmployeesUsed;
    
    // Getters and setters
    public String getLeaveType() {
        return leaveType;
    }
    
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
    
    public int getTotalDaysUsed() {
        return totalDaysUsed;
    }
    
    public void setTotalDaysUsed(int totalDaysUsed) {
        this.totalDaysUsed = totalDaysUsed;
    }
    
    public int getTotalEmployeesUsed() {
        return totalEmployeesUsed;
    }
    
    public void setTotalEmployeesUsed(int totalEmployeesUsed) {
        this.totalEmployeesUsed = totalEmployeesUsed;
    }
}