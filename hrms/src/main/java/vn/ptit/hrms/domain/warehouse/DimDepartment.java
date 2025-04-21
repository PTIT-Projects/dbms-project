package vn.ptit.hrms.domain.warehouse;

public class DimDepartment {
    private Integer departmentSk;
    private Integer departmentId;
    private String departmentName;
    private Integer managerSk;
    private String managerName;

    public Integer getDepartmentSk() {
        return departmentSk;
    }

    public void setDepartmentSk(Integer departmentSk) {
        this.departmentSk = departmentSk;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getManagerSk() {
        return managerSk;
    }

    public void setManagerSk(Integer managerSk) {
        this.managerSk = managerSk;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}