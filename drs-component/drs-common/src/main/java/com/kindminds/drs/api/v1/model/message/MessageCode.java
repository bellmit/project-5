package com.kindminds.drs.api.v1.model.message;



public enum MessageCode {

    COUPONS_ELIGIBLE_TO_PROCESS(1000, "Coupons available in the system eligible for processing"),

    COUPONS_ALREADY_PROCESSED(1002, "Coupons already have been processed"),

    COUPONS_NONE_TO_PROCESS(1003, "No coupons in the system to process");

    private int code;
    private String description;

    MessageCode(int value) {
        this.code = value;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public static MessageCode fromCode(int code) {
        for (MessageCode typeEnum : MessageCode.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum;
            }
        }
        return null;
    }

    MessageCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
