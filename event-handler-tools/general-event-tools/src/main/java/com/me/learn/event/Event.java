/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.event;

import java.util.EventObject;

/**
 * An event object is based on the Java standard {@link EventObject event}
 *
 * @Author: Administrator
 * Created: 2021/12/27
 **/
public class Event extends EventObject {

    private static final long serialVersionUID = 115353955041823749L;

    /**
     * the timestamp of event occurs
     */
    private long timestamp;

    /**
     * Constructs a prototypical event
     *
     * @param source
     */
    public Event(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
