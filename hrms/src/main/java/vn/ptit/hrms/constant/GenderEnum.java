package vn.ptit.hrms.constant;

public enum GenderEnum {
    OTHER("Other"), FEMALE("Female"), MALE("Male");
    private final String value;

    GenderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
