/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.notify.exception;

import com.me.learn.notify.commons.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/
public class NofityException extends Exception {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3913902031489277776L;

    private int errCode;

    private String errMsg;

    private Throwable causeThrowable;

    public NofityException() {
    }

    public NofityException(final int errCode, final String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public NofityException(final int errCode, final Throwable throwable) {
        super(throwable);
        this.errCode = errCode;
        this.setCauseThrowable(throwable);
    }

    public NofityException(final int errCode, final String errMsg, final Throwable throwable) {
        super(errMsg, throwable);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.setCauseThrowable(throwable);
    }

    public int getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        if (!StringUtils.isBlank(this.errMsg)) {
            return this.errMsg;
        }
        if (this.causeThrowable != null) {
            return this.causeThrowable.getMessage();
        }
        return Constants.NULL;
    }

    public void setErrCode(final int errCode) {
        this.errCode = errCode;
    }

    public void setErrMsg(final String errMsg) {
        this.errMsg = errMsg;
    }

    public void setCauseThrowable(final Throwable throwable) {
        this.causeThrowable = this.getCauseThrowable(throwable);
    }

    private Throwable getCauseThrowable(final Throwable t) {
        if (t.getCause() == null) {
            return t;
        }
        return this.getCauseThrowable(t.getCause());
    }

    @Override
    public String toString() {
        return "ErrCode:" + getErrCode() + ", ErrMsg:" + getErrMsg();
    }

    /*
     * client error code.
     * -400 -503 throw exception to user.
     */

    /**
     * invalid param??????????????????.
     */
    public static final int CLIENT_INVALID_PARAM = -400;

    /**
     * over client threshold?????????server?????????????????????.
     */
    public static final int CLIENT_OVER_THRESHOLD = -503;

    /*
     * server error code.
     * 400 403 throw exception to user
     * 500 502 503 change ip and retry
     */

    /**
     * invalid param??????????????????.
     */
    public static final int INVALID_PARAM = 400;

    /**
     * no right??????????????????.
     */
    public static final int NO_RIGHT = 403;

    /**
     * not found.
     */
    public static final int NOT_FOUND = 404;

    /**
     * conflict?????????????????????.
     */
    public static final int CONFLICT = 409;

    /**
     * server error???server?????????????????????.
     */
    public static final int SERVER_ERROR = 500;

    /**
     * bad gateway?????????????????????nginx?????????Server?????????.
     */
    public static final int BAD_GATEWAY = 502;

    /**
     * over threshold?????????server?????????????????????.
     */
    public static final int OVER_THRESHOLD = 503;

    public static final int RESOURCE_NOT_FOUND = -404;

    /**
     * http client error code,
     * ome exceptions that occurred when the use the Nacos RestTemplate and Nacos AsyncRestTemplate.
     */
    public static final int HTTP_CLIENT_ERROR_CODE = -500;
}
