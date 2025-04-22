package vn.ptit.hrms.constant;

public enum FactWorkTripStatusEnum {
    DONE("Đã hoàn thành"), HAPPENING("Đang diễn ra"), CANCELED("Đã hủy"), PREPARE("Chưa đến ngày");
    private final String value;

    FactWorkTripStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
