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

@State(value = Scope.Benchmark)
public class ProxyMethodEditedCallsTest {
    private Map<String, String> byteBuddy;
    private Map<String, String> cglibProxy;
    private Map<String, String> dynamicProxy;
    private Map<String, String> oneForOne;

    @Setup
    public void setup() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        byteBuddy = ByteBuddyProxy.createProxy(String.class, String.class);
        cglibProxy = CglibProxy.createProxy(String.class, String.class);
        dynamicProxy = DynamicProxy.getProxyMap(new HashMap<>());
        oneForOne = new OneForOne<>(new HashMap<>());
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void oneForOn(Blackhole blackhole) {
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
        blackhole.consume(testMap.put("a", "b"));
        blackhole.consume(testMap.put("b", "c"));
        blackhole.consume(testMap.put("c", "d"));
        blackhole.consume(testMap.put("password", "myPassword"));
        blackhole.consume(testMap.put("password", "myPassword1"));
        blackhole.consume(testMap.put("password", "myPassword2"));
    }
}
