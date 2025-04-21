package vn.ptit.hrms.domain.warehouse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FactSalary {
    private Integer salarySk;
    private Integer salaryId;
    private Integer employeeSk;
    private String employeeName;
    private String departmentName;
    private String positionName;
    private Integer dateSk;
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal deductions;
    private BigDecimal totalSalary;
    private String paymentStatus;

    public Integer getSalarySk() {
        return salarySk;
    }

    public void setSalarySk(Integer salarySk) {
        this.salarySk = salarySk;
    }

    public Integer getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Integer salaryId) {
        this.salaryId = salaryId;
    }

    public Integer getEmployeeSk() {
        return employeeSk;
    }

    public void setEmployeeSk(Integer employeeSk) {
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

    public Integer getDateSk() {
        return dateSk;
    }

    public void setDateSk(Integer dateSk) {
        this.dateSk = dateSk;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}