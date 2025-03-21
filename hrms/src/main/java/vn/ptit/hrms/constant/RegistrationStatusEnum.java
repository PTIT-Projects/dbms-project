package vn.ptit.hrms.constant;

public enum RegistrationStatusEnum {
    REJECTED("Từ chối"), APPROVED("Đã duyệt"), PENDING("Đang chờ");
    private final String value;

    RegistrationStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
