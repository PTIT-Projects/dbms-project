package vn.ptit.hrms.dto.warehouse;

public class RegistrationByEmployeeTypeDTO {
    private String employeeName;
    private String registrationType;
    private int totalRegistrations;
    
    public RegistrationByEmployeeTypeDTO() {
    }
    
    public RegistrationByEmployeeTypeDTO(String employeeName, String registrationType, int totalRegistrations) {
        this.employeeName = employeeName;
        this.registrationType = registrationType;
        this.totalRegistrations = totalRegistrations;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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