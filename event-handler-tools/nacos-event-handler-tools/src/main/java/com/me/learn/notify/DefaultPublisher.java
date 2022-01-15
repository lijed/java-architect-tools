/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.notify;

import com.me.learn.notify.listener.Subscriber;
import com.me.learn.notify.utils.CollectionUtils;
import com.me.learn.notify.utils.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import static com.me.learn.notify.NotifyCenter.ringBufferSize;

/**
 * Description:
 *
 * @Author: Jed Li
 * Created: 2021/12/24
 **/
public class DefaultPublisher extends Thread implements EventPublisher {
    protected static final Logger LOGGER = LoggerFactory.getLogger(NotifyCenter.class);

    private volatile boolean initialized = false;

    private volatile boolean shutdown = false;

    private Class<? extends Event> eventType;
    private int queueMaxSize = -1;

    //保持pushlish的事件
    private BlockingQueue<Event> queue;

    protected final ConcurrentHashSet<Subscriber> subscribers = new ConcurrentHashSet<Subscriber>();

    protected volatile Long lastEventSequence = -1L;

    private static final AtomicReferenceFieldUpdater<DefaultPublisher, Long> UPDATER = AtomicReferenceFieldUpdater
            .newUpdater(DefaultPublisher.class, Long.class, "lastEventSequence");


    @Override
    public void init(Class<? extends Event> type, int bufferSize) {
        //设置线程为后天线程，同时指定线程的名字
        setDaemon(true);
        setName("nacos.publisher-" + type.getName());

        this.eventType = type;
        this.queueMaxSize = bufferSize;
        this.queue = new ArrayBlockingQueue<>(queueMaxSize);

        //启动线程
        start();
    }

    @Override
    public synchronized void start() {
        if (!initialized) {
            // start just called once
            super.start();
            if (queueMaxSize == -1) {
                queueMaxSize = ringBufferSize;
                this.queue = new ArrayBlockingQueue<>(queueMaxSize);
            }
            initialized = true;
        }
    }

    @Override
    public long currentEventSize() {
        return subscribers.size();
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }


    @Override
    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public boolean publish(Event event) {
        checkIfStart();
        boolean success = queue.offer(event);

        //如果时间添加到队列失败，则马上执行
        if (!success) {
            LOGGER.warn("Unable to plug in due to interruption, synchronize sending time, event : {}", event);
            receiveEvent(event);
            return true;
        }

        return true;
    }

    private void checkIfStart() {
        if (!initialized) {
            throw new IllegalArgumentException("Publisher does not start");
        }
    }

    @Override
    public void notifySubscriber(final Subscriber subscriber, final Event event) {

        LOGGER.debug("[NotifyCenter] the {} will received by {}", event, subscriber);
        final Runnable job = () -> {
            subscriber.onEvent(event);
        };

        final Executor executor = subscriber.getExecutor();

        if (executor == null) {
            try {
                job.run();
            } catch (Throwable e) {
                LOGGER.error("Event callback exception : {}", e);
            }
        } else {
            executor.execute(job);
        }
    }

    @Override
    public void run() {
        openEventHandler();
    }

    private void openEventHandler() {

        try {
            // This variable is defined to resolve the problem which message overstock in the queue.
            int waitTime = 60;

            // To ensure that messages are not lost, enable EventHandler when
            // waiting for the first Subscriber to register
            for (; ; ) {
                if (shutdown || hasSubscriber() || waitTime <= 0) {
                    break;
                }
            }

            for (; ; ) {
                if (shutdown) {
                    break;
                }

                final Event event = queue.take();
                receiveEvent(event);

                UPDATER.compareAndSet(this, lastEventSequence, Math.max(lastEventSequence, event.sequence()));
            }
        } catch (Throwable ex) {
            LOGGER.error("Event listener exception : {}", ex);
        }
    }

    void receiveEvent(Event event) {

        final long currentEventSequence = event.sequence();

        //notify events
        for (Subscriber subscriber : subscribers) {
            if (subscriber.ignoreExpireEvent() && lastEventSequence > currentEventSequence) {
                LOGGER.debug("[NotifyCenter] the {} is unacceptable to this subscriber, because had expire",
                        event.getClass());
                continue;
            }

            // Because unifying smartSubscriber and subscriber, so here need to think of compatibility.
            // Remove original judge part of codes.
            notifySubscriber(subscriber, event);
        }
    }

    private boolean hasSubscriber() {
        return CollectionUtils.isNotEmpty(subscribers);
    }

    @Override
    public void shutdown() {
        this.shutdown = true;
        this.queue.clear();
    }

    public boolean isInitialized() {
        return initialized;
    }
}
