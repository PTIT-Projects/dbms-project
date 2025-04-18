package vn.ptit.hrms.domain.primary;

public class DepartmentManager {
    private Department department;
    private Employee manager;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
