package com.scaledcode.proxy;

import com.scaledcode.proxy.methods.ByteBuddyProxy;
import com.scaledcode.proxy.methods.CglibProxy;
import com.scaledcode.proxy.methods.DynamicProxy;
import com.scaledcode.proxy.methods.Inheritance;
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
public class UnrelatedMethodTest {
    private Map<String, String> byteBuddy;
    private Map<String, String> cglibProxy;
    private Map<String, String> dynamicProxy;
    private Map<String, String> oneForOne;
    private Map<String, String> inheritance;
    private Map<String, String> testFillMap = Map.of("a", "b",
                                                    "b", "c");

    @Setup
    public void setup() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        byteBuddy = ByteBuddyProxy.createProxy(String.class, String.class);
        cglibProxy = CglibProxy.createProxy(String.class, String.class);
        dynamicProxy = DynamicProxy.getProxyMap(new HashMap<>());
        oneForOne = new OneForOne<>(new HashMap<>());
        inheritance = new Inheritance<>();
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void oneForOne(Blackhole blackhole) {
        blackhole.consume(runTest(oneForOne));
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void byteBuddy(Blackhole blackhole) {
        blackhole.consume(runTest(byteBuddy));
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void cglib(Blackhole blackhole) {
        blackhole.consume(runTest(cglibProxy));
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void dynamicProxy(Blackhole blackhole) {
        blackhole.consume(runTest(dynamicProxy));
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput})
    public void inheritanceProxy(Blackhole blackhole) {
        blackhole.consume(runTest(inheritance));
    }

    private Map<String, String> runTest(Map<String, String> testMap) {
        for (int i=0; i < 10; i++) {
            testMap.putAll(testFillMap);
        }
        return testMap;
    }
}
