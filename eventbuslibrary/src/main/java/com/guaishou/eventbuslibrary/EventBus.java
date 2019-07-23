package com.guaishou.eventbuslibrary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实现EventBus的步骤为
 * 1 创建线程模式
 * 2 创建注解
 * 3 封装方法类
 * 4 存储方法，并通过反射进行调用
 */
public class EventBus {
    private static EventBus instance;
    private Map<Object, List<MethodSubscription>> cacheMap;
    private EventBus(){
        cacheMap = new HashMap<>();
    }
    public static EventBus getDefualt(){
        if(null == instance){
            synchronized(EventBus.class){
                if (null == instance){
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object object){
        //寻找注解及方法
        if (object==null)return;
        List<MethodSubscription> list = cacheMap.get(object);
        if (list==null){
            list = findSubcribeMethods(object);
            cacheMap.put(object,list);
        }
    }

    private List<MethodSubscription> findSubcribeMethods(Object object) {
        List<MethodSubscription> list = new ArrayList<>();
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            String name = clazz.getName();
            if (name.startsWith("java.")
                    || name.startsWith("javax.")
                    || name.startsWith("android.")) {
                break;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Subsribe subsribe = method.getAnnotation(Subsribe.class);
                if (subsribe == null) {
                    continue;
                }
                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    continue;
                }
                ThreadMode threadMode = subsribe.threadMode();
                MethodSubscription methodSubscription =
                        new MethodSubscription(method, threadMode, types[0]);
                list.add(methodSubscription);
            }
            clazz = clazz.getSuperclass();
        }

        return list;
    }
    public void post(Object type) {
        Set<Object>  set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while(iterator.hasNext()){
            Object object = iterator.next();
            List<MethodSubscription> list = cacheMap.get(object);
            for (MethodSubscription methodSubscription:list){
                if (methodSubscription.getType().isAssignableFrom(type.getClass())){
                    //todo 这里处理一些线程的切换
                    invoke(methodSubscription,object,type);
                }
            }
        }
    }

    private void invoke(MethodSubscription methodSubscription, Object object, Object type) {
        Method method = methodSubscription.getMethod();
        try {
            method.invoke(object,type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
