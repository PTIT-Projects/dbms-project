package vn.ptit.hrms.dto.warehouse;

public class TopRewardedEmployeeDTO {
    private String fullName;
    private String departmentName;
    private int rewardCount;
    
    public TopRewardedEmployeeDTO() {
    }
    
    public TopRewardedEmployeeDTO(String fullName, String departmentName, int rewardCount) {
        this.fullName = fullName;
        this.departmentName = departmentName;
        this.rewardCount = rewardCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }
}