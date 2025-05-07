package vn.ptit.hrms.dto.warehouse;

public class RegistrationByStatusDTO {
    private String status;
    private String registrationType;
    private int count;
    private double percentage;
    
    public RegistrationByStatusDTO() {
    }
    
    public RegistrationByStatusDTO(String status, String registrationType, int count, double percentage) {
        this.status = status;
        this.registrationType = registrationType;
        this.count = count;
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}