package com.example.havruta.errorAndException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
    EMAIL_DUPLICATION(400,"MEMBER-ERR-400","EMAIL DUPLICATED"),
    NO_GROUP_ERROR(404, "NO-GROUP-ERR-404", "GROUP_NOT_FOUND"),
    NO_USER_ERROR(404, "NO-USER-ERR-404", "USER_NOT_FOUND"),
    NO_CATEGORY_ERROR(404, "", "CATEGORY_NOT_FOUND"),
    NO_PROBLEM_ERROR(404, "", "PROBLEM_NOT_FOUND"),
    ALREADY_USER_ERROR(409, "", "ALREADY_USER"),
    ALREADY_MEMBER_ERROR(409, "", "ALREADY_MEMBER"),
    ALREADY_ADMIN_ERROR(409, "", "ALREADY_ADMIN"),
    NOT_MEMBER_ERROR(403, "", "NOT_MEMBER"),
    NOT_ADMIN_ERROR(403, "", "NOT_ADMIN"),
    CATEGORY_NAME_ERROR(409, "", "SAME_CATEGORY_NAME_ALREADY_EXISTS"),
    ROOT_CATEGORY_ERROR(403, "", "CANNOT_DELETE_ROOT_CATEGORY")
           ;

    private int status;
    private String errorCode;
    private String message;
}
