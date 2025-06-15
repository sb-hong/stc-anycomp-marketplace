package com.stctest.anycompmarketplace.enums;


public enum ErrorCode {
    
    SUCCESS(0, "SUCCESS"),

    SYSTEM_ERROR(10001, "Server Busy"),
    PARAMS_MISSING(10002, "Missing Parameters"),
    PARAMS_ENUM_CONVERT_FAIL(10003, "Enum Parameter Conversion Failed"),
    METHOD_NOT_SUPPORTED(10004, "Unsupported request method"),
    JSON_ERROR(10005, "JSON format error"),
    EMAIL_INVALID_FORMAT(10006, "Email format invalid"),
    USERNAME_PASSWORD_INVALID(10007, "Username or password invalid"),
    ACCESS_DENIED(10008, "Access denied"),
    UNAUTHORIZED(10009, "Unauthorized. Please login again"),

    BUYER_NOT_FOUND(40001, "Buyer not found"),
    ITEM_NOT_FOUND(40002, "Item not found"),
    PURCHASE_FAIL_ITEM_QUANTITY_INSUFFICIENT(40003, "Fail to purchase. Insufficient item quantity"),
    USERNAME_ALD_TAKEN(40004, "Username is already taken!"),
    EMAIL_ALD_TAKEN(40005, "Email is already taken!"),
    ROLE_NOT_FOUND(40006, "Role cannot be found"),;

    int code;
    String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

