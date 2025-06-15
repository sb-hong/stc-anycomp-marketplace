package com.stctest.anycompmarketplace.response;

import com.stctest.anycompmarketplace.enums.ErrorCode;

import lombok.Data;


@Data
public class BaseResponse<T> {

    private int code;

    private String msg;

    private T result;

    public BaseResponse() {
        setCode(ErrorCode.SUCCESS);
    }

    public BaseResponse(ErrorCode errorCode) {
        setCode(errorCode);
    }

    public BaseResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(T result) {
        setCode(ErrorCode.SUCCESS);
        this.result = result;
    }

    public void setCode(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }
}
