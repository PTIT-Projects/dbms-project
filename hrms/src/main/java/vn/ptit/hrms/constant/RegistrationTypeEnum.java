package vn.ptit.hrms.constant;

public enum RegistrationTypeEnum {
    RESIGNATION("Từ chức"), ADVANCED("Tạm ứng"), OVERTIME("Làm thêm giờ"), LEAVE("Nghỉ phép"), MEETINGROOM("Phòng họp"), MEAL("Ăn uống"), VEHICLE("Xe");
    private final String value;

    RegistrationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
