package vn.ptit.hrms.constant;

public enum CompetencyTypeEnum {
    ATTITUDE("Attitude"), SKILL("Skill"), KNOWLEDGE("Knowledge");
    private final String value;

    CompetencyTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
