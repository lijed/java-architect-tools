/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.longpooling;

import com.me.learn.common.utils.JacksonUtils;
import com.me.learn.common.utils.StringUtils;
import com.me.learn.longpooling.entity.User;
import com.me.learn.longpooling.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Description:
 *
 * @Author: Jed Li
 * Created: 2021/12/25
 **/

@Service
public class LongPollingService {
    Logger logger = LoggerFactory.getLogger(LongPollingService.class);
    private static final long TIME_OUT_MILLISECONDS = 50000;

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private static ExecutorService WECHAT_LOGIN_NOFICATION_EXECUTORSERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    final Queue<ClientLongPolling> allSubs;

    public LongPollingService() {
        this.allSubs = new ConcurrentLinkedQueue<>();
    }

    public void checkUserInfo(HttpServletRequest request, HttpServletResponse response, String userName) {
        String remoteIp = RequestUtil.getRemoteIp(request);

        //Must be called by http thread, or send response
        final AsyncContext asyncContext = request.startAsync();

        //AsyncContext.setTimeout() is incorrect, Control by oneself
        asyncContext.setTimeout(0L);

        long timeoutMillis = TIME_OUT_MILLISECONDS - 500L;

        executorService.execute(new ClientLongPolling(request, response, TIME_OUT_MILLISECONDS, asyncContext, userName));
    }

    public void handleWechatLoginNotification(String userName, String resultCode) {
        WechatLoginNotification notification = new WechatLoginNotification(userName, resultCode);
        WECHAT_LOGIN_NOFICATION_EXECUTORSERVICE.submit(new WechatLoginNotificationTask(notification));
    }

    class WechatLoginNotification {
        private String userName;
        private String resultCode;

        public WechatLoginNotification(String userName, String resultCode) {
            this.userName = userName;
            this.resultCode = resultCode;
        }

        public String getUserName() {
            return userName;
        }

        public String getResultCode() {
            return resultCode;
        }
    }

    class ClientLongPolling implements Runnable {

        private final HttpServletRequest request;
        private final HttpServletResponse response;
        private final AsyncContext asyncContext;
        private final String userName;
        private final Long timeoutTime;

        private ScheduledFuture<?> asyncTimeoutFuture;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ClientLongPolling)) {
                return false;
            }
            ClientLongPolling that = (ClientLongPolling) o;
            return userName.equals(that.userName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userName);
        }

        public ClientLongPolling(HttpServletRequest request, HttpServletResponse response, long timeOutMilliseconds, AsyncContext asyncContext, String userName) {
            this.request = request;
            this.response = response;
            this.userName = userName;
            this.timeoutTime = timeOutMilliseconds;
            this.asyncContext = asyncContext;
        }

        @Override
        public void run() {
            asyncTimeoutFuture = scheduledExecutorService.schedule(() -> {
                //No wechat verification notification is reached, actively to ask wechat
                String result = getVerification(userName);
                if ("success".equals(result)) {
                    User user = new User();
                    user.setUserName("jedli");
                    user.setMemberLevel(1);
                    sendResponse(user);
                } else {
                    sendResponse(null);
                }


            }, timeoutTime, TimeUnit.MILLISECONDS);

            allSubs.add(this);
        }

        private String getVerification(String userName) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "success";
        }

        @Override
        public String toString() {
            return "ClientLongPolling{" +
                    "request=" + request +
                    ", response=" + response +
                    ", asyncContext=" + asyncContext +
                    ", userName='" + userName + '\'' +
                    ", timeout=" + timeoutTime +
                    '}';
        }

        private void sendResponse(User user) {

            // Cancel time out task.
            if (null != asyncTimeoutFuture) {
                asyncTimeoutFuture.cancel(false);
            }

            generateResponse(user);

        }

        void generateResponse(User user) {
            if (null == user) {
                // Tell web container to send http response.
                asyncContext.complete();
                return;
            }

            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

            try {
                // Disable cache.
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache,no-store");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(JacksonUtils.toJson(user));
                asyncContext.complete();

            } catch (Exception ex) {
                logger.error(ex.toString(), ex);
                asyncContext.complete();
            }
        }
    }


    class WechatLoginNotificationTask implements Runnable {
        private WechatLoginNotification notification;
        public WechatLoginNotificationTask(WechatLoginNotification notification) {
            this.notification = notification;
        }

        @Override
        public void run() {
            for (ClientLongPolling clientLongPolling : allSubs) {
                if (clientLongPolling.userName.equals(notification.getUserName())) {
                    allSubs.remove(clientLongPolling);
                    if (StringUtils.equals("1", notification.getResultCode())) {
                        User user = new User();
                        user.setUserName("JedLi");
                        clientLongPolling.sendResponse(user);
                    }
                }
            }
        }

    }
}
