package vn.ptit.hrms.constant;

public enum GenderEnum {
    OTHER("Khác"), FEMALE("Nữ"), MALE("Nam");
    private final String value;

    GenderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
