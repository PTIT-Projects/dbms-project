package vn.ptit.hrms.constant;

public enum TrainingCompletionStatusEnum {
    NOTSTARTED("Not Started"), INPROGRESS("In progress"), COMPLETED("Completed");
    private final String value;

    TrainingCompletionStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
