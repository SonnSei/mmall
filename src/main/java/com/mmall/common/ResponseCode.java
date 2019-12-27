package com.mmall.common;

/**
 * @Classname ResponseCode
 * @Description TODO
 * @Date 2019/12/27 10:36
 * @Author SonnSei
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"), ERROR(0, "ERROR"), NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
