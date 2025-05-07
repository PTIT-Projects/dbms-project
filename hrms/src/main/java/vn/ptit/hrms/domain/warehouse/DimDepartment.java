package vn.ptit.hrms.domain.warehouse;

public class DimDepartment {
    private int departmentSk;
    private int departmentId;
    private String departmentName;
    private int managerSk;
    private String managerName;

    public int getDepartmentSk() {
        return departmentSk;
    }

    public void setDepartmentSk(int departmentSk) {
        this.departmentSk = departmentSk;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getManagerSk() {
        return managerSk;
    }

    public void setManagerSk(int managerSk) {
        this.managerSk = managerSk;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
