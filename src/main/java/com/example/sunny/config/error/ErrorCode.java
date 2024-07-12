package com.example.sunny.config.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", "Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "ENTITY_NOT_FOUND", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "Server Error"),
    INVALID_TYPE_VALUE(400, "INVALID_TYPE_VALUE", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "HANDLE_ACCESS_DENIED", "Access is Denied"),
    IP_NOT_ALLOWD(403,"IPNOTALLOWED","IP NOT ALLOWED"),
    EXPIRED_TOKEN(401,"EXPIRED-TOKEN","EXPIRED-TOKEN"),
    INVAILDCONTENTCLASS(400,"INVAILDCONTENTCLASS","INVAILDCONTENTCLASS"),
    SQLEXCEPTION(400,"SQLEXCEPTION","SQLEXCEPTION"),
    FILEIOEXCEPTION(400,"FILEIOEXCEPTION","FILEIOEXCEPTION"),
    EXCEPTION(400,"EXCEPTION","EXCEPTION"),
    AUTHALREADYEXISTS(400,"AUTHALREADYEXISTS","AUTHALREADYEXISTS"),
    USERALREADYEXISTS(400,"USERALREADYEXISTS","USERALREADYEXISTS"),
    DATAALREDAYEXISTS(400,"DATAALREDAYEXISTS", "DATA_ALREDAY_EXISTS"),
    GROUPNOTFOUND(400,"GROUPNOTFOUND","GROUPNOTFOUND"),
    AUTHEXCEPTION(400,"AUTHEXCEPTION","AUTHEXCEPTION"),
    USERNOTFOUND(400,"USERNOTFOUND","USERNOTFOUND"),
    USEREXCEPTION(400,"USEREXCEPTION","USEREXCEPTION"),
    USERDISABLED(400,"USERDISABLED","USERDISABLED"),
    PASSWORDEMPTY(400,"PASSWORDEMPTY","PASSWORDEMPTY"),
    APIEXCEPTION(400,"APIEXCEPTION","APIEXCEPTION"),
    PERMISSION_ERROR(403, "PERMISSION_ERROR", "PERMISSION_ERROR"),
    STATBATCHJOB_ERROR(400,"STATBATCHJOB_ERROR", "STATBATCHJOB_ERROR"),
    WRONG_ID_OR_PASSWORD(400, "WRONG_ID_OR_PASSWORD", "Login failed: Invalid userId or password");


    private final String code;
    private String message;
    private int status;

    ErrorCode(final int status, final String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }


    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


    public void setMessage(String message) {
        this.message = message;
    }



}
