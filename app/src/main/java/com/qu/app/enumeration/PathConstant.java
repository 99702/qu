package com.qu.app.enumeration;

public class PathConstant {
    // user constants
    public static final String LOGIN_USER = "login";
    public static final String DELETE_USER = "delete/{userId}";
    public static final String UPDATE_USER = "update/{userId}";
    public static final String SINGLE_USER = "{userId}";
    public static final String REGISTER_USER = "register";
    public static final String ALL_USER = "all";
    public static final String SEARCH_USER_BY = "search";
    public static final String GET_USER_ATTR_EXACT = "find/exact";


    // Post constants
    public static final String CREATE_POST = "create/";
    public static final String UPDATE_POST = "update/{postTitle}/{userId}";
    public static final String DELETE_POST = "delete/{postTitle}/{userId}";
    public static final String SEARCH_POST_BY = "find";
    public static final String ALL_POST = "all";
    public static final String CURRENT_LOGGED_USER = "current";

    // Comment constants
    public static final String CREATE_COMMENT = "create/{postId}/";

    /*
    statistics
     */
    public static final String USER_STATS = "stats";
}
