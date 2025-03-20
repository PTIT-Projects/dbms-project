package vn.ptit.hrms.constant;

public enum RegistrationStatusEnum {
    REJECTED("Rejected"), APPROVED("Approved"), PENDING("Pending");
    private final String value;

    RegistrationStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
