package vn.ptit.hrms.dto.warehouse;

public class RewardOvertimeCorrelationDTO {
    private int employeeSk;
    private String fullName;
    private int rewardCount;
    private double totalOvertimeHours;
    
    public RewardOvertimeCorrelationDTO() {
    }
    
    public RewardOvertimeCorrelationDTO(int employeeSk, String fullName, int rewardCount, double totalOvertimeHours) {
        this.employeeSk = employeeSk;
        this.fullName = fullName;
        this.rewardCount = rewardCount;
        this.totalOvertimeHours = totalOvertimeHours;
    }

    public int getEmployeeSk() {
        return employeeSk;
    }

    public void setEmployeeSk(int employeeSk) {
        this.employeeSk = employeeSk;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public double getTotalOvertimeHours() {
        return totalOvertimeHours;
    }

    public void setTotalOvertimeHours(double totalOvertimeHours) {
        this.totalOvertimeHours = totalOvertimeHours;
    }
}