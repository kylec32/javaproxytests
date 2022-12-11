package com.scaledcode.proxy.methods;

import com.scaledcode.proxy.utils.FakePasswordHasher;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.TargetType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.Super;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ByteBuddyProxy {
    public static long timeConsumed;
    public static <K, V> Map<K, V> createProxy(Class<K> keyClass, Class<V> valueClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?> proxyMap = new ByteBuddy()
                .subclass(HashMap.class)
                .method(named("get"))
                .intercept(MethodDelegation.to(AlternativeImplementation.class))
                .method(named("put"))
                .intercept(MethodDelegation.to(PreventPasswordKey.class))
                .name("ProxyMap")
                .method(named("containsValue"))
                .intercept(FixedValue.value(true))
                .make()
                .load(ByteBuddyProxy.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();


        return (Map<K, V>)proxyMap.getConstructor().newInstance();
    }

    public static class AlternativeImplementation {
        @RuntimeType
        public static Object intercept(@SuperCall Callable<?> wrapped) throws Exception {
            long start = System.nanoTime();

            Object returnValue =  wrapped.call();

            ByteBuddyProxy.timeConsumed += (System.nanoTime() - start);

            return returnValue;
        }
    }

    public static class PreventPasswordKey {
        @RuntimeType
        @SneakyThrows
        public static Object intercept(@AllArguments Object[] allArguments, @Origin Method method, @Super(proxyType = TargetType.class) Object delegate) {
            Object firstArgument = allArguments[0];

            if (method.getName().equals("put") && firstArgument instanceof String && "password".equalsIgnoreCase(firstArgument.toString())) {
                return method.invoke(delegate, "hashedPassword", FakePasswordHasher.hashPassword(allArguments[1].toString()));
            } else {
                return method.invoke(delegate, allArguments);
            }
        }
    }
}
