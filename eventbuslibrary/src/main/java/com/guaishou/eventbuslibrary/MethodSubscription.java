package com.guaishou.eventbuslibrary;

import java.lang.reflect.Method;

public class MethodSubscription {
    private Method method;
    private ThreadMode mode;
    private Class<?> type;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ThreadMode getMode() {
        return mode;
    }

    public void setMode(ThreadMode mode) {
        this.mode = mode;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public MethodSubscription(Method method, ThreadMode mode, Class<?> type) {
        this.method = method;
        this.mode = mode;
        this.type = type;
    }
}
