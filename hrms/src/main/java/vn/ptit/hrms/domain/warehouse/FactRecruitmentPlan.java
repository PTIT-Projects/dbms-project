package vn.ptit.hrms.domain.warehouse;

public class FactRecruitmentPlan {
    private int recruitmentSk;
    private int recruitmentId;
    private int positionSk;
    private String positionName;
    private int departmentSk;
    private String departmentName;
    private int startDateSk;
    private int endDateSk;
    private int quantity;
    private int recruitmentDuration;
    private int remainingPositions;

    public int getRecruitmentSk() {
        return recruitmentSk;
    }

    public void setRecruitmentSk(int recruitmentSk) {
        this.recruitmentSk = recruitmentSk;
    }

    public int getRecruitmentId() {
        return recruitmentId;
    }

    public void setRecruitmentId(int recruitmentId) {
        this.recruitmentId = recruitmentId;
    }

    public int getPositionSk() {
        return positionSk;
    }

    public void setPositionSk(int positionSk) {
        this.positionSk = positionSk;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getDepartmentSk() {
        return departmentSk;
    }

    public void setDepartmentSk(int departmentSk) {
        this.departmentSk = departmentSk;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getStartDateSk() {
        return startDateSk;
    }

    public void setStartDateSk(int startDateSk) {
        this.startDateSk = startDateSk;
    }

    public int getEndDateSk() {
        return endDateSk;
    }

    public void setEndDateSk(int endDateSk) {
        this.endDateSk = endDateSk;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRecruitmentDuration() {
        return recruitmentDuration;
    }

    public void setRecruitmentDuration(int recruitmentDuration) {
        this.recruitmentDuration = recruitmentDuration;
    }

    public int getRemainingPositions() {
        return remainingPositions;
    }

    public void setRemainingPositions(int remainingPositions) {
        this.remainingPositions = remainingPositions;
    }
}
