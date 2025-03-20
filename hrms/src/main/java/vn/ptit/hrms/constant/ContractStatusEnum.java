package vn.ptit.hrms.constant;

public enum ContractStatusEnum {
    TERMINATED("Terminated"), EXPIRED("Expired"), ACTIVE("Active");
    private final String value;

    ContractStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
