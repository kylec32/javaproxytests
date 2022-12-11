package com.scaledcode.proxy.methods;

import com.scaledcode.proxy.utils.FakePasswordHasher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CglibProxy {
    public static long timeConsumed;
    public static <K, V> Map<K, V> createProxy(Class<K> keyClass, Class<V> valueClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HashMap.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if (method.getName().equals("get")) {
                return processGetRequest(obj, args, proxy);
            } else if (method.getName().equals("put") && args[0] instanceof String && "password".equals(args[0].toString())) {
                return method.invoke(obj, "hashedPassword", FakePasswordHasher.hashPassword(args[1].toString()));
            } else if(method.getName().equals("containsValue")) {
                return true;
            } else {
                return proxy.invokeSuper(obj, args);
            }
        });

        return (Map<K, V>) enhancer.create();
    }

    private static Object processGetRequest(Object obj, Object[] args, MethodProxy proxy) throws Throwable {
        long startTime = System.nanoTime();

        Object returnValue = proxy.invokeSuper(obj, args);

        timeConsumed += (System.nanoTime() - startTime);

        return returnValue;
    }
}
