package com.scaledcode.proxy.methods;

import com.scaledcode.proxy.utils.FakePasswordHasher;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

@RequiredArgsConstructor
public class DynamicProxy implements InvocationHandler {
    public static long timeConsumed;
    private final Map<Object, Object> wrapped;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("get")) {
            Object getParameter = args[0];
            return customGetMethod(getParameter);
        } else if (method.getName().equals("put") && args[0] instanceof String && "password".equalsIgnoreCase(args[0].toString())) {
            return wrapped.put("hashedPassword", FakePasswordHasher.hashPassword(args[1].toString()));
        }
        return method.invoke(wrapped, args);
    }

    private Object customGetMethod(Object key) {
        long startTime = System.nanoTime();

        Object returnValue;
        if (key instanceof String && "specialKey".equals(key.toString())) {
            returnValue = wrapped.get("nonSpecialKey");
        } else {
            returnValue = wrapped.get(key);
        }

        DynamicProxy.timeConsumed += (System.nanoTime() - startTime);

        return returnValue;
    }


    public static <K, V> Map<K, V> getProxyMap(Map<K, V> wrapped) {
        return (Map<K, V>) Proxy.newProxyInstance(
                DynamicProxy.class.getClassLoader(),
                new Class[] { Map.class },
                new DynamicProxy((Map<Object, Object>) wrapped));
    }
}
