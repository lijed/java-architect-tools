/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.notify.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/12/24
 **/
public class ConcurrentHashSet<E> extends AbstractSet<E> {

    private ConcurrentHashMap<E, Boolean> map;

    public ConcurrentHashSet() {
        super();
        this.map = new ConcurrentHashMap<E, Boolean>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }


    @Override
    public Iterator<E> iterator() {
        List<E> list = new ArrayList<E>();
        for (Map.Entry<E, Boolean> e : map.entrySet()) {
            list.add(e.getKey());
        }

        return list.iterator();
    }

    @Override
    public boolean add(E o) {
        return map.putIfAbsent(o, Boolean.TRUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    @Override
    public void clear() {
        map.clear();
    }
}
