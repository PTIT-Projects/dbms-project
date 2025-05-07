package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class SalaryTrendByMonthDTO {
    private int year;
    private int month;
    private BigDecimal avgBasic;
    private BigDecimal avgTotal;
    private BigDecimal departmentTotalPayroll;
    
    // Getters and setters
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
    
    public BigDecimal getDepartmentTotalPayroll() {
        return departmentTotalPayroll;
    }
    
    public void setDepartmentTotalPayroll(BigDecimal departmentTotalPayroll) {
        this.departmentTotalPayroll = departmentTotalPayroll;
    }
}