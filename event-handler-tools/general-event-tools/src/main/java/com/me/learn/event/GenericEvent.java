/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.event;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/27
 **/
public class GenericEvent<S> extends Event {
    private static final long serialVersionUID = -4300042911853784133L;

    /**
     * Constructs a prototypical event
     *
     * @param source
     */
    public GenericEvent(Object source) {
        super(source);
    }

    @Override
    public S getSource() {
        return (S) super.getSource();
    }

}
