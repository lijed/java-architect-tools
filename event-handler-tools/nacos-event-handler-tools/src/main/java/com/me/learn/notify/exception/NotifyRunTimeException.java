/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.notify.exception;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/
public class NotifyRunTimeException extends RuntimeException {
    private static final long serialVersionUID = 924405963809248620L;

    public static final String ERROR_MESSAGE_FORMAT = "errCode: %d, errMsg: %s ";

    private int errCode;

    public NotifyRunTimeException(int errCode) {
        super();
        this.errCode = errCode;
    }

    public NotifyRunTimeException(int errCode, String errMsg) {
        super(String.format(ERROR_MESSAGE_FORMAT, errCode, errMsg));
        this.errCode = errCode;
    }

    public NotifyRunTimeException(int errCode, Throwable throwable) {
        super(throwable);
        this.errCode = errCode;
    }

    public NotifyRunTimeException(int errCode, String errMsg, Throwable throwable) {
        super(String.format(ERROR_MESSAGE_FORMAT, errCode, errMsg), throwable);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
