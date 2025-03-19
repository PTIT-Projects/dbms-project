package vn.ptit.hrms.constant;


public enum EmployeeStatusEnum {
    TERMINATED("Terminated"), EXPIRED("Expired"), ACTIVE("Active");

    EmployeeStatusEnum(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
