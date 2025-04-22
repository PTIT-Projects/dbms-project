package vn.ptit.hrms.domain.warehouse;

import vn.ptit.hrms.constant.FactSalaryEnum;

public class FactSalary {
    private int salarySk;
    private int salaryId;
    private int employeeSk;
    private String employeeName;
    private String departmentName;
    private String positionName;
    private int dateSk;
    private double basicSalary;
    private double allowance;
    private double deductions;
    private double totalSalary;
    private FactSalaryEnum paymentStatus;

    public int getSalarySk() {
        return salarySk;
    }

    public void setSalarySk(int salarySk) {
        this.salarySk = salarySk;
    }

    public int getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    public int getEmployeeSk() {
        return employeeSk;
    }

    public void setEmployeeSk(int employeeSk) {
        this.employeeSk = employeeSk;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getDateSk() {
        return dateSk;
    }

    public void setDateSk(int dateSk) {
        this.dateSk = dateSk;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public FactSalaryEnum getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(FactSalaryEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
