/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.notify.listener;

import com.me.learn.notify.Event;

import java.util.List;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/
public abstract class SmartSubscriber extends Subscriber {

    /**
     * returns which event type which the smartsubscriber interested in.
     *
     * @return  the interested event types
     */
    public abstract List<Class<? extends Event>> subscribeTypes();

    @Override
    public final Class<? extends  Event> subscribeType() {
        return null;
    }

    @Override
    public boolean ignoreExpireEvent() {
        return false;
    }
}
