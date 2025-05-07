package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class SalaryRatioByDepartmentDTO {
    private String departmentName;
    private BigDecimal avgBasic;
    private BigDecimal avgTotal;
    private double basicSalaryRatio;
    private double allowanceRatio;
    private double deductionsRatio;
    
    // Getters and setters
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public BigDecimal getAvgBasic() {
        return avgBasic;
    }
    
    public void setAvgBasic(BigDecimal avgBasic) {
        this.avgBasic = avgBasic;
    }
    
    public BigDecimal getAvgTotal() {
        return avgTotal;
    }
    
    public void setAvgTotal(BigDecimal avgTotal) {
        this.avgTotal = avgTotal;
    }
    
    public double getBasicSalaryRatio() {
        return basicSalaryRatio;
    }
    
    public void setBasicSalaryRatio(double basicSalaryRatio) {
        this.basicSalaryRatio = basicSalaryRatio;
    }
    
    public double getAllowanceRatio() {
        return allowanceRatio;
    }
    
    public void setAllowanceRatio(double allowanceRatio) {
        this.allowanceRatio = allowanceRatio;
    }
    
    public double getDeductionsRatio() {
        return deductionsRatio;
    }
    
    public void setDeductionsRatio(double deductionsRatio) {
        this.deductionsRatio = deductionsRatio;
    }
}