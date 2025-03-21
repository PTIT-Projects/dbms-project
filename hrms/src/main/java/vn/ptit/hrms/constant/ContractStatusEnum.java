package vn.ptit.hrms.constant;

public enum ContractStatusEnum {
    TERMINATED("Chấm dứt"), EXPIRED("Hết hạn"), ACTIVE("Hiệu lực");
    private final String value;

    ContractStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
