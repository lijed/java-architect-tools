/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.common.exception;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/
public class CommonRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -6019600126624184673L;

    public static final String ERROR_MESSAGE_FORMAT = "errCode: %d, errMsg: %s ";

    private int errCode;

    public CommonRuntimeException(int errCode) {
        this.errCode = errCode;
    }

    public CommonRuntimeException(String message, int errCode) {
        super(String.format(ERROR_MESSAGE_FORMAT, errCode, message));
        this.errCode = errCode;
    }

    public CommonRuntimeException(String message, Throwable cause, int errCode) {
        super(String.format(ERROR_MESSAGE_FORMAT, errCode, message), cause);
        this.errCode = errCode;
    }

    public CommonRuntimeException(Throwable cause, int errCode) {
        super(cause);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
