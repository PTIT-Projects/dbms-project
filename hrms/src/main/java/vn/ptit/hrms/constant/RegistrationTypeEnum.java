package vn.ptit.hrms.constant;

public enum RegistrationTypeEnum {
    RESIGNATION("Resignation"), ADVANCED("Advanced"), OVERTIME("Overtime"), LEAVE("Leave"), MEETINGROOM("MeetingRoom"), MEAL("Meal"), VEHICLE("Vehicle");
    private final String value;

    RegistrationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
