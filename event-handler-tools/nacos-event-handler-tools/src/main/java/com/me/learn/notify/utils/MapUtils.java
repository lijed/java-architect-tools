/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.me.learn.notify.utils;


import com.me.learn.notify.commons.NotThreadSafe;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Map utils.
 *
 * @author Jed Li
 */
public class MapUtils {
    
    /**
     * Null-safe check if the specified Dictionary is empty.
     *
     * <p>Null returns true.
     *
     * @param map the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }
    
    /**
     * Null-safe check if the specified Dictionary is empty.
     *
     * <p>Null returns true.
     *
     * @param coll the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(Dictionary coll) {
        return (coll == null || coll.isEmpty());
    }
    
    /**
     * Null-safe check if the specified Dictionary is not empty.
     *
     * <p>Null returns false.
     *
     * @param map the collection to check, may be null
     * @return true if non-null and non-empty
     */
    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }
    
    /**
     * Null-safe check if the specified Dictionary is not empty.
     *
     * <p>Null returns false.
     *
     * @param coll the collection to check, may be null
     * @return true if non-null and non-empty
     */
    public static boolean isNotEmpty(Dictionary coll) {
        return !isEmpty(coll);
    }
    
    /**
     * Put into map if value is not null.
     *
     * @param target target map
     * @param key    key
     * @param value  value
     */
    public static void putIfValNoNull(Map target, Object key, Object value) {
        Objects.requireNonNull(key, "key");
        if (value != null) {
            target.put(key, value);
        }
    }
    
    /**
     * Put into map if value is not empty.
     *
     * @param target target map
     * @param key    key
     * @param value  value
     */
    public static void putIfValNoEmpty(Map target, Object key, Object value) {
        Objects.requireNonNull(key, "key");
        if (value instanceof String) {
            if (StringUtils.isNotEmpty((String) value)) {
                target.put(key, value);
            }
            return;
        }
        if (value instanceof Collection) {
            if (CollectionUtils.isNotEmpty((Collection) value)) {
                target.put(key, value);
            }
            return;
        }
        if (value instanceof Map) {
            if (isNotEmpty((Map) value)) {
                target.put(key, value);
            }
            return;
        }
        if (value instanceof Dictionary) {
            if (isNotEmpty((Dictionary) value)) {
                target.put(key, value);
            }
            return;
        }
    }
    
    /**
     * ComputeIfAbsent lazy load.
     *
     * @param target          target Map data.
     * @param key             map key.
     * @param mappingFunction function which is need to be executed.
     * @param param1          function's parameter value1.
     * @param param2          function's parameter value1.
     * @return
     */
    @NotThreadSafe
    public static Object computeIfAbsent(Map target, Object key, BiFunction mappingFunction, Object param1,
                                         Object param2) {
        
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(mappingFunction, "mappingFunction");
        Objects.requireNonNull(param1, "param1");
        Objects.requireNonNull(param2, "param2");
        
        Object val = target.get(key);
        if (val == null) {
            Object ret = mappingFunction.apply(param1, param2);
            target.put(key, ret);
            return ret;
        }
        return val;
    }
}
