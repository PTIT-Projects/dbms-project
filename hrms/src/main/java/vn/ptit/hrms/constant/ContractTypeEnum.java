package vn.ptit.hrms.constant;

public enum ContractTypeEnum {
    FULLTIME("Toàn thời gian"), PARTTIME("Bán thời gian");
    private final String value;

    ContractTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
