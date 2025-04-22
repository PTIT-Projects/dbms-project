package vn.ptit.hrms.constant;

public enum FactSalaryEnum {
    PAID("Đã thanh toán"), NOTPAID("Chưa thanh toán");
    private final String value;

    FactSalaryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
