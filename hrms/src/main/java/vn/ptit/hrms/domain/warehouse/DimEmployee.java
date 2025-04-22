package vn.ptit.hrms.domain.warehouse;


import java.time.LocalDate;

public class DimEmployee {
    private int employeeSk;
    private int employeeId;
    private String fullName;
    private LocalDate dateOfBirth;
    private char gender;
    private String address;
    private String phone;
    private String email;
    private int departmentSk;
    private String departmentName;
    private int positionSk;
    private String positionName;
    private int age;
    private LocalDate hireDate;
    private String currentContractType;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private int contractDuration;
    private double totalYearsWorked;
    private String insuranceNumber;
    private String insuranceType;
    private LocalDate insuranceStartDate;
    private LocalDate insuranceEndDate;
    private int insuranceDuration;

    public int getEmployeeSk() {
        return employeeSk;
    }

    public void setEmployeeSk(int employeeSk) {
        this.employeeSk = employeeSk;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getPositionSk() {
        return positionSk;
    }

    public void setPositionSk(int positionSk) {
        this.positionSk = positionSk;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getCurrentContractType() {
        return currentContractType;
    }

    public void setCurrentContractType(String currentContractType) {
        this.currentContractType = currentContractType;
    }

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public int getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(int contractDuration) {
        this.contractDuration = contractDuration;
    }

    public double getTotalYearsWorked() {
        return totalYearsWorked;
    }

    public void setTotalYearsWorked(double totalYearsWorked) {
        this.totalYearsWorked = totalYearsWorked;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public LocalDate getInsuranceStartDate() {
        return insuranceStartDate;
    }

    public void setInsuranceStartDate(LocalDate insuranceStartDate) {
        this.insuranceStartDate = insuranceStartDate;
    }

    public LocalDate getInsuranceEndDate() {
        return insuranceEndDate;
    }

    public void setInsuranceEndDate(LocalDate insuranceEndDate) {
        this.insuranceEndDate = insuranceEndDate;
    }

    public int getInsuranceDuration() {
        return insuranceDuration;
    }

    public void setInsuranceDuration(int insuranceDuration) {
        this.insuranceDuration = insuranceDuration;
    }
}
