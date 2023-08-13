package com.socialmedia.constant;

public class ApiUrls {

    public static final String VERSION = "api/v1";
    public static final String USER_PROFILE = VERSION + "/user-profile";

    //AuthController
    public static final String CREATE_USER = "/create-user";
    public static final String UPDATE = "/update";
    public static final String FIND_BY_ID = "/find-by-id";
    public static final String FIND_ALL = "/find-all";
    public static final String FORGOT_PASSWORD = "/forgot-password";
    public static final String PASSWORD_CHANGE = "/password-change";
    public static final String ACTIVATE_STATUS = "/activate-status/{authId}";
}
