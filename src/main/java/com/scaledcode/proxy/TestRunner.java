package com.scaledcode.proxy;

import com.scaledcode.proxy.methods.ByteBuddyProxy;
import com.scaledcode.proxy.methods.CglibProxy;
import com.scaledcode.proxy.methods.DynamicProxy;
import com.scaledcode.proxy.methods.OneForOne;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class TestRunner {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        //Map<String, String> tester = ByteBuddyProxy.createProxy(String.class, String.class);
        //Map<String, String> tester = DynamicProxy.getProxyMap(new HashMap<>());
        //Map<String, String> tester = new OneForOne<>(new HashMap<>());
        Map<String, String> tester = CglibProxy.createProxy(String.class, String.class);

        tester.put("hi", "greeting");
        System.out.println(tester.get("hi"));

        tester.put("password", "123");

        System.out.println(tester.get("hashedPassword"));
    }
}
