/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.common.exception;

import java.lang.reflect.Type;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/
public class DeserializationException extends CommonRuntimeException {

    private static final long serialVersionUID = -6956278119619283640L;


    public static final int ERROR_CODE = 101;
    private static final String DEFAULT_MSG = "Deserialize failed. ";
    private static final String MSG_FOR_SPECIFIED_CLASS = "Deserialize for class [%s] failed. ";

    private Class<?> targetClass;

    public DeserializationException() {
        super(ERROR_CODE);
    }

    public DeserializationException(Class<?> targetClass) {
        super(String.format(MSG_FOR_SPECIFIED_CLASS, targetClass), ERROR_CODE);
        this.targetClass = targetClass;
    }


    public DeserializationException(Throwable cause) {
        super(DEFAULT_MSG, cause, ERROR_CODE);
    }

    public DeserializationException(Throwable cause, int errCode) {
        super(cause, errCode);
    }

    public DeserializationException(Type type, Throwable throwable) {
        super(String.format(MSG_FOR_SPECIFIED_CLASS, type.getClass()), throwable, ERROR_CODE);
        this.targetClass = type.getClass();
    }
}
