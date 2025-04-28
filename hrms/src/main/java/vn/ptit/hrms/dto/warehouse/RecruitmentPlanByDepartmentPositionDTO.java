package vn.ptit.hrms.dto.warehouse;

public class RecruitmentPlanByDepartmentPositionDTO {
    private String departmentName;
    private String positionName;
    private int quantity;
    private int remainingPositions;
    private int filledPositions;

    // Default constructor
    public RecruitmentPlanByDepartmentPositionDTO() {
    }
    
    // Constructor with all fields
    public RecruitmentPlanByDepartmentPositionDTO(String departmentName, String positionName, int quantity, 
                                                 int remainingPositions, int filledPositions) {
        this.departmentName = departmentName;
        this.positionName = positionName;
        this.quantity = quantity;
        this.remainingPositions = remainingPositions;
        this.filledPositions = filledPositions;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRemainingPositions() {
        return remainingPositions;
    }

    public void setRemainingPositions(int remainingPositions) {
        this.remainingPositions = remainingPositions;
    }

    public int getFilledPositions() {
        return filledPositions;
    }

    public void setFilledPositions(int filledPositions) {
        this.filledPositions = filledPositions;
    }
    
    // Get completion percentage (filled/total positions)
    public double getCompletionPercentage() {
        return quantity > 0 ? (filledPositions * 100.0 / quantity) : 0;
    }
    
    // Get progress as a formatted string for display
    public String getProgressStatus() {
        return String.format("%d/%d (%d%%)", filledPositions, quantity, 
                             (int)Math.round(getCompletionPercentage()));
    }
}
