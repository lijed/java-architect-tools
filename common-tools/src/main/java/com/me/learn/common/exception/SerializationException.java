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
public class SerializationException extends CommonRuntimeException {
    private static final long serialVersionUID = -5551418466087152069L;

    public static final int ERROR_CODE = 100;

    private static final String DEFAULT_MSG = "Common serialize failed. ";

    private static final String MSG_FOR_SPECIFIED_CLASS = "Common serialize for class [%s] failed. ";

    private Class<?> serializedClass;

    public SerializationException() {
        super(ERROR_CODE);
    }

    public SerializationException(Class<?> serializedClass) {
        super(String.format(MSG_FOR_SPECIFIED_CLASS, serializedClass.getName()), ERROR_CODE);
        this.serializedClass = serializedClass;
    }

    public SerializationException(Throwable cause) {
        super(DEFAULT_MSG, cause, ERROR_CODE);
    }

    public SerializationException(Throwable cause, int errCode) {
        super(cause, errCode);
    }

    public SerializationException(Class<?> serializedClass, Throwable throwable) {
        super(String.format(MSG_FOR_SPECIFIED_CLASS, serializedClass.getName()), throwable, ERROR_CODE);
        this.serializedClass = serializedClass;
    }

    public Class<?> getSerializedClass() {
        return serializedClass;
    }
}
