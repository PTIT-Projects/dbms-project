package vn.ptit.hrms.constant;

public enum DecisionTypeEnum {
    DISCIPLINE("Discipline"), REWARD("Reward"), APPOINTMENT("Appointment");
    private final String value;

    DecisionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
