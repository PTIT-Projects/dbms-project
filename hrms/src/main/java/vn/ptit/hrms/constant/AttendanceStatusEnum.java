package vn.ptit.hrms.constant;

public enum AttendanceStatusEnum {
    OVERTIME("Làm thêm giờ"), LATE("Đi muộn"), ABSENT("Vắng mặt"), PRESENT("Có mặt");
    private final String value;

    AttendanceStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
