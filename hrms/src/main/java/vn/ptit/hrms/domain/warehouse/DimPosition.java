package vn.ptit.hrms.domain.warehouse;

public class DimPosition {
    private int positionSk;
    private int positionId;
    private String positionName;
    private int departmentSk;
    private String departmentName;

    public int getPositionSk() {
        return positionSk;
    }

    public void setPositionSk(int positionSk) {
        this.positionSk = positionSk;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
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
}
