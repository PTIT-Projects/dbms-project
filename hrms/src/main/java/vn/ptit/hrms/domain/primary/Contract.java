package vn.ptit.hrms.domain.primary;

import vn.ptit.hrms.constant.ContractStatusEnum;
import vn.ptit.hrms.constant.ContractTypeEnum;

import java.time.LocalDate;

public class Contract {
    private Integer id;
    private Employee employee;
    private ContractTypeEnum contractType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double salary;
    private ContractStatusEnum status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ContractTypeEnum getContractType() {
        return contractType;
    }

    public void setContractType(ContractTypeEnum contractType) {
        this.contractType = contractType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public ContractStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ContractStatusEnum status) {
        this.status = status;
    }
}
