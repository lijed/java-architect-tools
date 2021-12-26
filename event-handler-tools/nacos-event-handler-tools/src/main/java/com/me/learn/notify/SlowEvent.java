/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.notify;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/25
 **/
public class SlowEvent extends Event{

    private static final long serialVersionUID = -968613135348731167L;

    @Override
    public long sequence() {
        return 0;
    }
}
