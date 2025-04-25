package vn.ptit.hrms.dto.warehouse;

public class LeaveByExperienceDTO {
    private String experienceLevel;
    private double avgAllocatedLeave;
    private double avgUsedLeave;
    private double usagePercentage;
    
    // Getters and setters
    public String getExperienceLevel() {
        return experienceLevel;
    }
    
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
    
    public double getAvgAllocatedLeave() {
        return avgAllocatedLeave;
    }
    
    public void setAvgAllocatedLeave(double avgAllocatedLeave) {
        this.avgAllocatedLeave = avgAllocatedLeave;
    }
    
    public double getAvgUsedLeave() {
        return avgUsedLeave;
    }
    
    public void setAvgUsedLeave(double avgUsedLeave) {
        this.avgUsedLeave = avgUsedLeave;
    }
    
    public double getUsagePercentage() {
        return usagePercentage;
    }
    
    public void setUsagePercentage(double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }
}