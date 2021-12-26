/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.longpooling.utils;

import com.me.learn.common.utils.StringUtils;
import com.me.learn.longpooling.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/
public class RequestUtil {

    private static final String X_REAL_IP = "X-Real-IP";

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";

    private static final String X_FORWARDED_FOR_SPLIT_SYMBOL = ",";

    public static final String CLIENT_APPNAME_HEADER = "Client-AppName";

    public static final String NACOS_USER_KEY = "nacosuser";

    /**
     * get real client ip
     *
     * <p>first use X-Forwarded-For header    https://zh.wikipedia.org/wiki/X-Forwarded-For next nginx X-Real-IP last
     * {@link HttpServletRequest#getRemoteAddr()}
     *
     * @param request {@link HttpServletRequest}
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader(X_FORWARDED_FOR);
        if (!StringUtils.isBlank(xForwardedFor)) {
            return xForwardedFor.split(X_FORWARDED_FOR_SPLIT_SYMBOL)[0].trim();
        }
        String nginxHeader = request.getHeader(X_REAL_IP);
        return StringUtils.isBlank(nginxHeader) ? request.getRemoteAddr() : nginxHeader;
    }

    /**
     * Gets the name of the client application in the header.
     *
     * @param request {@link HttpServletRequest}
     * @return may be return null
     */
    public static String getAppName(HttpServletRequest request) {
        return request.getHeader(CLIENT_APPNAME_HEADER);
    }

    /**
     * Gets the user of the client application in the Attribute.
     *
     * @param request {@link HttpServletRequest}
     * @return may be return null
     */
    public static User getUser(HttpServletRequest request) {
        Object userObj = request.getAttribute(NACOS_USER_KEY);
        if (userObj == null) {
            return null;
        }

        User user = (User) userObj;
        return user;
    }

    /**
     * Gets the username of the client application in the Attribute.
     *
     * @param request {@link HttpServletRequest}
     * @return may be return null
     */
    public static String getSrcUserName(HttpServletRequest request) {
        User user = getUser(request);
        return user == null ? null : user.getUserName();
    }

}
