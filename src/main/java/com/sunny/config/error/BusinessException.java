package com.sunny.config.error;

public class BusinessException extends RuntimeException {
    private ErrorCode errorCode;

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message) {
        super(message);
        this.errorCode.setMessage(message);
    }

    public BusinessException(ErrorCode errorCode ,String message) {
        //  super(message);
        this.errorCode = errorCode;
        try {
            this.errorCode.setMessage(message);
        } catch (NullPointerException e) {
            this.errorCode.setMessage(errorCode.getCode());
        }

    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
