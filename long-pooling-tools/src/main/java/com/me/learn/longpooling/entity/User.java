/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.longpooling.entity;

import lombok.Data;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/
@Data
public class User {
    private String userName;
    private String password;
    private String market;
    private int memberLevel;
}
