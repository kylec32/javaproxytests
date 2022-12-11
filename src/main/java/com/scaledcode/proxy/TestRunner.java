package com.scaledcode.proxy;

import com.scaledcode.proxy.methods.ByteBuddyProxy;
import com.scaledcode.proxy.methods.CglibProxy;
import com.scaledcode.proxy.methods.DynamicProxy;
import com.scaledcode.proxy.methods.OneForOne;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class TestRunner {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        new ByteBuddy()
                .subclass(HashMap.class)
                .method(named("get"))
                .intercept(MethodDelegation.to(ByteBuddyProxy.AlternativeImplementation.class))
                .method(named("put"))
                .intercept(MethodDelegation.to(ByteBuddyProxy.PreventPasswordKey.class))
                .name("ProxyMap")
                .make()
                .saveIn(new File("C:/bytebuddyclass.class"));
    }
}
