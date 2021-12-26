/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.common.executor;

import com.me.learn.common.utils.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @Author: Jed Li
 * Created: 2021/12/25
 **/
public class NameThreadFactory implements ThreadFactory {

    public final AtomicInteger id = new AtomicInteger(0);

    private String name;

    public NameThreadFactory(String name) {
        if (!name.endsWith(StringUtils.DOT)) {
            name += StringUtils.DOT;
        }
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = name + id.getAndIncrement();
        Thread thread = new Thread(r, threadName);
        thread.setDaemon(true);
        return thread;
    }
}
