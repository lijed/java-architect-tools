package com.me.learn;

import java.io.PrintStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Author Jed Li
 * @Date 2021/12/27 10:56
 * @Version 1.0
 * @Project java-architect-tools
 */
public class Test {
    public static void main(String[] args) {

        //接口
        Type[] genericInterfaces = ArrayList.class.getGenericInterfaces();

        //声明泛型的父类
        Type genericSuperclass = ArrayList.class.getGenericSuperclass();


        List<Type> parameterizedTypes = new ArrayList<>(1+ genericInterfaces.length);
        parameterizedTypes.add(genericSuperclass);
        parameterizedTypes.addAll(Arrays.asList(genericInterfaces));
        List<Type> types = parameterizedTypes.stream().filter(type -> type instanceof ParameterizedType).collect(Collectors.toList());
        types.forEach(type -> {
            ParameterizedType ptype = (ParameterizedType) type;

            Type[] actualTypeArguments = ptype.getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                System.out.println("type argument: " + actualTypeArgument);
            }
            PrintStream printf = System.out.printf("arguments %s, ownertype %s, rawtypes: %s", actualTypeArguments, ptype.getOwnerType(), ptype.getRawType());
            printf.println();
            printf.close();
        });

    }
}
