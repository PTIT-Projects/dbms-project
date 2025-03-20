package vn.ptit.hrms.constant;

public enum EmployeeCompetencyLevelEnum {
    ADVANCED("Advanced"), INTERMEDIATE("Intermediate"), BASIC("Basic");
    private final String value;

    EmployeeCompetencyLevelEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
