package com.sunny.config.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "적절한 입력값을 입력해주세요"),
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", "잘못된 접근방법 입니다."),
    ENTITY_NOT_FOUND(400, "ENTITY_NOT_FOUND", "해당 개체를 찾지 못했습니다."),
    RESOURCE_NOT_FOUND(404, "RESOURCE_NOT_FOUND", "404_NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 에러. 관리자에게 문의해주세요"),
    INVALID_TYPE_VALUE(400, "INVALID_TYPE_VALUE", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "HANDLE_ACCESS_DENIED", "Access is Denied"),
    EXPIRED_TOKEN(401,"EXPIRED-TOKEN","EXPIRED-TOKEN"),
    FILEIOEXCEPTION(400,"FILEIOEXCEPTION","FILEIOEXCEPTION"),
    EXCEPTION(400,"EXCEPTION","EXCEPTION"),
    AUTHALREADYEXISTS(400,"AUTHALREADYEXISTS","AUTHALREADYEXISTS"),
    USERALREADYEXISTS(400,"USERALREADYEXISTS","USERALREADYEXISTS"),
    DATAALREDAYEXISTS(409,"DATAALREDAYEXISTS", "DATA_ALREDAY_EXISTS"),
    AUTHEXCEPTION(401,"AUTHEXCEPTION","권한 설정 문제"),
    USERNOTFOUND(400,"USERNOTFOUND","USERNOTFOUND"),
    USEREXCEPTION(400,"USEREXCEPTION","USEREXCEPTION"),
    USERDISABLED(400,"USERDISABLED","USERDISABLED"),
    APIEXCEPTION(400,"APIEXCEPTION","APIEXCEPTION"),
    PERMISSION_ERROR(403, "PERMISSION_ERROR", "PERMISSION_ERROR"),
    EMAILALREADYEXISTS(400,"EMAILALREADYEXISTS","EMAILALREADYEXISTS"),
    EMAIL_NOT_APPROVED(400,"EMAIL_NOT_APPROVED","EMAIL_NOT_APPROVED"),
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
