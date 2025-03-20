package vn.ptit.hrms.constant;

public enum AttendanceStatus {
    OVERTIME("Overtime"), LATE("Late"), ABSENT("Absent"), PRESENT("Present");
    private final String value;

    AttendanceStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
