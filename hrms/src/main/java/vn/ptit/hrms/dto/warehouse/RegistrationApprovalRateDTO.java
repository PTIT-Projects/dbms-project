package vn.ptit.hrms.dto.warehouse;

public class RegistrationApprovalRateDTO {
    private String registrationType;
    private int totalRegistrations;
    private int approvedCount;
    private double approvalRatePercentage;
    
    public RegistrationApprovalRateDTO() {
    }
    
    public RegistrationApprovalRateDTO(String registrationType, int totalRegistrations, int approvedCount, double approvalRatePercentage) {
        this.registrationType = registrationType;
        this.totalRegistrations = totalRegistrations;
        this.approvedCount = approvedCount;
        this.approvalRatePercentage = approvalRatePercentage;
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

    public int getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(int approvedCount) {
        this.approvedCount = approvedCount;
    }

    public double getApprovalRatePercentage() {
        return approvalRatePercentage;
    }

    public void setApprovalRatePercentage(double approvalRatePercentage) {
        this.approvalRatePercentage = approvalRatePercentage;
    }
    
    public int getRejectedCount() {
        return totalRegistrations - approvedCount;
    }
}