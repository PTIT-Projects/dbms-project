package vn.ptit.hrms.constant;


public enum EmployeeStatusEnum {
    ONLEAVE("On Leave"), INACTIVE("Inactive"), ACTIVE("Active");

    EmployeeStatusEnum(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
