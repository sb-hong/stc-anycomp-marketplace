package com.stctest.anycompmarketplace.exception;

import com.stctest.anycompmarketplace.enums.ErrorCode;

public class CustomFailureException extends RuntimeException {
    private ErrorCode errorCode;

    public CustomFailureException(ErrorCode errorCode) {
        super(String.format("%1$s(%2$d)[%3$s]", errorCode.name(), errorCode.getCode(), errorCode.getMsg()));
        this.errorCode = errorCode;
    }

    /**
     * @return {@link #errorCode}
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }


}
