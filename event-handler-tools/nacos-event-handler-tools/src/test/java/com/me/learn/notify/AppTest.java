/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.notify;

import com.me.learn.notify.listener.Subscriber;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/

public class AppTest {

    @Test
    public void testSubscribe() throws IOException {
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                LocalDataChangeEvent localDataChangeEvent = (LocalDataChangeEvent) event;
                PrintStream printf = System.out.printf("Handle Event %s with source %s in thread: %s",
                        event.getClass(),
                        localDataChangeEvent.getSource(),
                        Thread.currentThread().getName());
                printf.println();
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return LocalDataChangeEvent.class;
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });

        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                LocalDataChangeEvent localDataChangeEvent = (LocalDataChangeEvent) event;
                PrintStream printf = System.out.printf("Log Event %s with source %s in thread: %s",
                        event.getClass(),
                        localDataChangeEvent.getSource(),
                        Thread.currentThread().getName());
                printf.println();
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return LocalDataChangeEvent.class;
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });

        NotifyCenter.publishEvent(new LocalDataChangeEvent("The userName is updated"));

        //Need to wait event to be handler
        System.in.read();
    }

    @Test
    public void testSubscribeInAsyncMode() throws IOException {
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                LocalDataChangeEvent localDataChangeEvent = (LocalDataChangeEvent) event;
                PrintStream printf = System.out.printf("Handle Event %s with source %s in thread: %s",
                        event.getClass(),
                        localDataChangeEvent.getSource(),
                        Thread.currentThread().getName());
                printf.println();
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return LocalDataChangeEvent.class;
            }

            @Override
            public Executor getExecutor() {
                return Executors.newSingleThreadExecutor(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "LocalDataChangeEvent-subscribe thread");
                    }
                });
            }
        });

        NotifyCenter.publishEvent(new LocalDataChangeEvent("The userName is updated"));

        System.in.read();
    }


    class LocalDataChangeEvent extends Event {

        private String source;

        private static final long serialVersionUID = -981059613514550642L;

        public LocalDataChangeEvent(String source) {
            this.source = source;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
