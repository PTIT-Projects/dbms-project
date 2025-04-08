package vn.ptit.hrms.constant;

public enum DecisionTypeEnum {
    DISCIPLINE("Kỷ luật"), REWARD("Khen thưởng"), APPOINTMENT("Bổ nhiệm");
    private final String value;

    DecisionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
