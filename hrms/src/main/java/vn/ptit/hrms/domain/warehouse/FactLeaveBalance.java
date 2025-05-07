package vn.ptit.hrms.domain.warehouse;

public class FactLeaveBalance {
    private int leaveBalanceSk;
    private int leaveBalanceId;
    private int employeeSk;
    private String employeeName;
    private String departmentName;
    private String positionName;
    private String leaveType;
    private int dateSk;
    private String granularity;
    private int totalLeaveDays;
    private int usedLeaveDays;
    private int remainingLeaveDays;

    public int getLeaveBalanceSk() {
        return leaveBalanceSk;
    }

    public void setLeaveBalanceSk(int leaveBalanceSk) {
        this.leaveBalanceSk = leaveBalanceSk;
    }

    public int getLeaveBalanceId() {
        return leaveBalanceId;
    }

    public void setLeaveBalanceId(int leaveBalanceId) {
        this.leaveBalanceId = leaveBalanceId;
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

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public int getDateSk() {
        return dateSk;
    }

    public void setDateSk(int dateSk) {
        this.dateSk = dateSk;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public int getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public void setTotalLeaveDays(int totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }

    public int getUsedLeaveDays() {
        return usedLeaveDays;
    }

    public void setUsedLeaveDays(int usedLeaveDays) {
        this.usedLeaveDays = usedLeaveDays;
    }

    public int getRemainingLeaveDays() {
        return remainingLeaveDays;
    }

    public void setRemainingLeaveDays(int remainingLeaveDays) {
        this.remainingLeaveDays = remainingLeaveDays;
    }
}
