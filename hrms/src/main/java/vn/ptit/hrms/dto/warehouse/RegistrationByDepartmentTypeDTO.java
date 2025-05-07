package vn.ptit.hrms.dto.warehouse;

public class RegistrationByDepartmentTypeDTO {
    private String departmentName;
    private String registrationType;
    private int totalRegistrations;
    
    public RegistrationByDepartmentTypeDTO() {
    }
    
    public RegistrationByDepartmentTypeDTO(String departmentName, String registrationType, int totalRegistrations) {
        this.departmentName = departmentName;
        this.registrationType = registrationType;
        this.totalRegistrations = totalRegistrations;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public int getTotalRegistrations() {
        return totalRegistrations;
    }

    public void setTotalRegistrations(int totalRegistrations) {
        this.totalRegistrations = totalRegistrations;
    }
}