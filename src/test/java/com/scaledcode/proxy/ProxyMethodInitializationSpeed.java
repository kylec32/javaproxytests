package com.scaledcode.proxy;

import com.scaledcode.proxy.methods.ByteBuddyProxy;
import com.scaledcode.proxy.methods.CglibProxy;
import com.scaledcode.proxy.methods.DynamicProxy;
import com.scaledcode.proxy.methods.OneForOne;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ProxyMethodInitializationSpeed {
    @Benchmark
    //@BenchmarkMode(value = {Mode.Throughput, Mode.AverageTime})
    @BenchmarkMode(value = {Mode.Throughput})
    public void oneForOne(Blackhole blackhole) {
        Map<String, String> testCreation = new OneForOne<>(new HashMap<>());

        blackhole.consume(testCreation);
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void byteBuddy(Blackhole blackhole) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> testCreation = ByteBuddyProxy.createProxy(String.class, String.class);

        blackhole.consume(testCreation);
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void cglib(Blackhole blackhole) {
        Map<String, String> testCreation = CglibProxy.createProxy(String.class, String.class);

        blackhole.consume(testCreation);
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void dynamicProxy(Blackhole blackhole) {
        Map<String, String> testCreation = DynamicProxy.getProxyMap(new HashMap<>());

        blackhole.consume(testCreation);
    }
}
