package com.me.learn.event;

import com.me.learn.function.ThrowableConsumer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.me.learn.function.ThrowableFunction.execute;
import static java.util.stream.Stream.of;

/**
 * @Author Jed Li
 * @Date 2021/12/27 11:21
 * @Version 1.0
 * @Project java-architect-tools
 */
public class GenericListener implements EventListener<Event> {


    private final Method onEventMethod;
    private final Map<Class<?>, Set<Method>> handleEventMethods;

    public GenericListener() {
        this.onEventMethod = findOnEventMethod();
        this.handleEventMethods = fineHandleEventMethods();
    }

    private Map<Class<?>, Set<Method>> fineHandleEventMethods() {
        // Event class for key, the eventMethods' Set as value
        Map<Class<?>, Set<Method>> eventMethods = new HashMap<>();
        of(getClass().getMethods())
                .filter(this::isHandleEventMethod)
                .forEach(method -> {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Set<Method> methods = eventMethods.computeIfAbsent(paramType, key -> new LinkedHashSet<>());
                    methods.add(method);
                });
        return eventMethods;
    }

    private Method findOnEventMethod() {
        return execute(getClass(), listenerClass -> listenerClass.getMethod("onEvent", Event.class));
    }

    @Override
    public void onEvent(Event event) {
        handleEventMethods.getOrDefault(event.getClass(), Collections.emptySet()).forEach(method -> {
                    ThrowableConsumer.execute(method, m -> {
                        method.invoke(this, event);
                    });
                }
        );
    }

    /**
     * The {@link Event event} handle methods must meet following conditions:
     * <ul>
     * <li>not {@link #onEvent(Event)} method</li>
     * <li><code>public</code> accessibility</li>
     * <li><code>void</code> return type</li>
     * <li>no {@link Exception exception} declaration</li>
     * <li>only one {@link Event} type argument</li>
     * </ul>
     *
     * @param method
     * @return
     */
    private boolean isHandleEventMethod(Method method) {

        if (onEventMethod.equals(method)) { // not {@link #onEvent(Event)} method
            return false;
        }

        if (!Modifier.isPublic(method.getModifiers())) { // not public
            return false;
        }

        if (!void.class.equals(method.getReturnType())) { // void return type
            return false;
        }

        Class[] exceptionTypes = method.getExceptionTypes();

        if (exceptionTypes.length > 0) { // no exception declaration
            return false;
        }

        Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1) { // not only one argument
            return false;
        }

        if (!Event.class.isAssignableFrom(paramTypes[0])) { // not Event type argument
            return false;
        }

        return true;
    }
}
