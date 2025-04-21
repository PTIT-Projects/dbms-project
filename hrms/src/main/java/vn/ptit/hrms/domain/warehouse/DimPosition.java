package vn.ptit.hrms.domain.warehouse;

public class DimPosition {
    private Integer positionSk;
    private Integer positionId;
    private String positionName;
    private Integer departmentSk;
    private String departmentName;

    public Integer getPositionSk() {
        return positionSk;
    }

    public void setPositionSk(Integer positionSk) {
        this.positionSk = positionSk;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getDepartmentSk() {
        return departmentSk;
    }

    public void setDepartmentSk(Integer departmentSk) {
        this.departmentSk = departmentSk;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}