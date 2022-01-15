package com.me.learn.event;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

/**
 * @Author Jed Li
 * @Date 2021/12/27 10:25
 * @Version 1.0
 * @Project java-architect-tools
 */
@FunctionalInterface
public interface EventListener<E extends Event> extends java.util.EventListener, Prioritized {

    /**
     * Handle a {@link Event event} when it's be published
     *
     * @param event a {@link Event event}
     */
    void onEvent(E event);

    /**
     * The priority of {@link EventListener current listener}.
     *
     * @return the value is more greater, the priority is more lower.
     * {@link Integer#MIN_VALUE} indicates the highest priority. The default value is {@link Integer#MAX_VALUE}.
     * The comparison rule , refer to {@link #compareTo}.
     */
    @Override
    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    /**
     * Find the {@link Class type} {@link Event event} from the specified {@link EventListener event listener}
     *
     * @param listener the {@link Class class} of {@link EventListener event listener}
     * @return <code>null</code> if not found
     */
    static Class<? extends Event> findEventType(EventListener<?> listener) {
        return findEventType(listener.getClass());
    }

    /**
     * Find the {@link Class type} {@link Event event} from the specified {@link EventListener event listener}
     *
     * @param listenerClass the {@link Class class} of {@link EventListener event listener}
     * @return <code>null</code> if not found
     */
    static Class<? extends Event> findEventType(Class<?> listenerClass) {
        Class<? extends Event> eventType = null;

        if (listenerClass != null && EventListener.class.isAssignableFrom(listenerClass)) {
            eventType = findParameterizedTypes(listenerClass)
                    .stream()
                    .map(EventListener::findEventType)
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse((Class) findEventType(listenerClass.getSuperclass()));
        }

        return eventType;
    }

    public static Set<ParameterizedType> findParameterizedTypes(Class<?> sourceClass) {
        // Add Generic Interfaces
        List<Type> genericTypes = new LinkedList<>(asList(sourceClass.getGenericInterfaces()));
        // Add Generic Super Class
        genericTypes.add(sourceClass.getGenericSuperclass());

        Set<ParameterizedType> parameterizedTypes = genericTypes.stream()
                .filter(type -> type instanceof ParameterizedType)// filter ParameterizedType
                .map(ParameterizedType.class::cast)  // cast to ParameterizedType
                .collect(Collectors.toSet());

        if (parameterizedTypes.isEmpty()) { // If not found, try to search super types recursively
            genericTypes.stream()
                    .filter(type -> type instanceof Class)
                    .map(Class.class::cast)
                    .forEach(superClass -> parameterizedTypes.addAll(findParameterizedTypes(superClass)));
        }

        return unmodifiableSet(parameterizedTypes);                     // build as a Set

    }

    /**
     * Find the type {@link Event event} from the specified {@link ParameterizedType} presents
     * a class of {@link EventListener event listener}
     *
     * @param parameterizedType the {@link ParameterizedType} presents a class of {@link EventListener event listener}
     * @return <code>null</code> if not found
     */
    static Class<? extends Event> findEventType(ParameterizedType parameterizedType) {
        Class<? extends Event> eventType = null;

        Type rawType = parameterizedType.getRawType();
        if ((rawType instanceof Class) && EventListener.class.isAssignableFrom((Class) rawType)) {
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            for (Type typeArgument : typeArguments) {
                if (typeArgument instanceof Class) {
                    Class argumentClass = (Class) typeArgument;
                    if (Event.class.isAssignableFrom(argumentClass)) {
                        eventType = argumentClass;
                        break;
                    }
                }
            }
        }

        return eventType;
    }
}
