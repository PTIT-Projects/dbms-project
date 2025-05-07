package vn.ptit.hrms.dto.warehouse;

public class RegistrationTrendDTO {
    private int year;
    private int month;
    private String registrationType;
    private int totalRegistrations;
    
    public RegistrationTrendDTO() {
    }
    
    public RegistrationTrendDTO(int year, int month, String registrationType, int totalRegistrations) {
        this.year = year;
        this.month = month;
        this.registrationType = registrationType;
        this.totalRegistrations = totalRegistrations;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
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
    
    public String getMonthYear() {
        return month + "/" + year;
    }
}