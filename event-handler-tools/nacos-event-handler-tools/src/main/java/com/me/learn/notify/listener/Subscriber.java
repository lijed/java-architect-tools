/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.notify.listener;

import com.me.learn.notify.Event;

import java.util.concurrent.Executor;

/**
 * Description:
 *
 * @Author: Jed Li
 * Created: 2021/12/24
 **/
public abstract class Subscriber<T extends Event> {

    /**
     * Whether to ignore the expired event
     *
     * @return default value is {@link Boolean#FALSE}
     */
    public boolean ignoreExpireEvent() {
        return false;
    }

    /**
     * Event callback
     *
     * @param event {@link Event}
     */
    public abstract void onEvent(T event);


    /**
     * It is update to the listener to determine whether the callback is asynchronous or synchronous
     *
     * @return {@link Executor}
     */
    public Executor getExecutor() {
        return null;
    }

    /**
     * Type of this subscriber's subscription.
     *
     * @return Class which extends {@link Event}
     */
    public abstract Class<? extends Event> subscribeType();
}
