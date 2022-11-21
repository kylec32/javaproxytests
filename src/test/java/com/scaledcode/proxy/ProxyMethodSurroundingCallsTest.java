package com.scaledcode.proxy;

import com.scaledcode.proxy.methods.ByteBuddyProxy;
import com.scaledcode.proxy.methods.CglibProxy;
import com.scaledcode.proxy.methods.DynamicProxy;
import com.scaledcode.proxy.methods.OneForOne;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@State(Scope.Benchmark)
public class ProxyMethodSurroundingCallsTest {
    private Map<String, String> byteBuddy;
    private Map<String, String> cglibProxy;
    private Map<String, String> dynamicProxy;
    private Map<String, String> oneForOne;
    private Map<String, String> testFillMap = Map.of("a", "b",
                                                    "b", "c",
                                                    "c", "d",
                                                    "d", "e",
                                                    "e", "f");

    @Setup
    public void setup() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        byteBuddy = ByteBuddyProxy.createProxy(String.class, String.class);
        byteBuddy.putAll(testFillMap);
        cglibProxy = CglibProxy.createProxy(String.class, String.class);
        cglibProxy.putAll(testFillMap);
        dynamicProxy = DynamicProxy.getProxyMap(new HashMap<>());
        dynamicProxy.putAll(testFillMap);
        oneForOne = new OneForOne<>(new HashMap<>());
        oneForOne.putAll(testFillMap);
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void oneForOne(Blackhole blackhole) {
        runTest(oneForOne, blackhole);
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void byteBuddy(Blackhole blackhole) {
        runTest(byteBuddy, blackhole);
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void cglib(Blackhole blackhole) {
        runTest(cglibProxy, blackhole);
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void dynamicProxy(Blackhole blackhole) {
        runTest(dynamicProxy, blackhole);
    }

    private void runTest(Map<String, String> testMap, Blackhole blackhole) {
        blackhole.consume(testMap.get("a"));
        blackhole.consume(testMap.get("b"));
        blackhole.consume(testMap.get("c"));
        blackhole.consume(testMap.get("d"));
        blackhole.consume(testMap.get("e"));
    }
}
