package vn.ptit.hrms.dto.warehouse;

public class RecruitmentPlanByDepartmentPositionDTO {
    private String departmentName;
    private String positionName;
    private int quantity;
    private int remainingPositions;
    private int filledPositions;

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
}
