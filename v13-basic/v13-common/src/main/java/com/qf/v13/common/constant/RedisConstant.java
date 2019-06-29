package com.qf.v13.common.constant;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface RedisConstant {
    String USER_SESSION_KEY = "user:token";
    Long USER_SESSION_MAX_TIME = 1800L;
    int COOKIE_MAXTIME = 3600 * 24;
    String SHOPCART_TOKEN_KEY = "shopcart:token";
}
