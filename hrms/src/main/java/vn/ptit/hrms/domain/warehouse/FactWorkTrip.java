package vn.ptit.hrms.domain.warehouse;

import vn.ptit.hrms.constant.FactWorkTripStatusEnum;

public class FactWorkTrip {
    private int workTripSk;
    private int workTripId;
    private int employeeSk;
    private String employeeName;
    private String departmentName;
    private String positionName;
    private int startDateSk;
    private int endDateSk;
    private int tripDuration;
    private String destination;
    private String purpose;
    private double totalCost;
    private FactWorkTripStatusEnum status;

    public int getWorkTripSk() {
        return workTripSk;
    }

    public void setWorkTripSk(int workTripSk) {
        this.workTripSk = workTripSk;
    }

    public int getWorkTripId() {
        return workTripId;
    }

    public void setWorkTripId(int workTripId) {
        this.workTripId = workTripId;
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

    public int getStartDateSk() {
        return startDateSk;
    }

    public void setStartDateSk(int startDateSk) {
        this.startDateSk = startDateSk;
    }

    public int getEndDateSk() {
        return endDateSk;
    }

    public void setEndDateSk(int endDateSk) {
        this.endDateSk = endDateSk;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public FactWorkTripStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FactWorkTripStatusEnum status) {
        this.status = status;
    }
}
