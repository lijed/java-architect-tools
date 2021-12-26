/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.longpooling.controller;

import com.me.learn.longpooling.LongPollingService;
import com.me.learn.longpooling.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/

@RestController
public class LongPollingController {

    @Autowired
    private LongPollingService longPollingService;

    @GetMapping("/checkLogin/{userName}")
    public void checkLogin(@PathVariable(name="userName") String userName, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        longPollingService.checkUserInfo(request, response, userName);
    }

    @GetMapping("/wechat/notification/{userName}/{resultCode}")
    public void wechatScanLoginNotification(@PathVariable(name = "userName") String userName, @PathVariable(name="resultCode") String resultCode) {
        longPollingService.handleWechatLoginNotification(userName, resultCode);
    }
}
