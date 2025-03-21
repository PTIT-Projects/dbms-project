package vn.ptit.hrms.constant;


public enum EmployeeStatusEnum {
    ONLEAVE("Nghỉ phép"), INACTIVE("Nghỉ việc"), ACTIVE("Đang làm việc");

    EmployeeStatusEnum(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
