package vn.ptit.hrms.dto.warehouse;

import java.math.BigDecimal;

public class TotalOvertimeByMonthDTO {
    private int year;
    private int month;
    private BigDecimal totalOvertimeHours;

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

    public BigDecimal getTotalOvertimeHours() {
        return totalOvertimeHours;
    }

    public void setTotalOvertimeHours(BigDecimal totalOvertimeHours) {
        this.totalOvertimeHours = totalOvertimeHours;
    }
}