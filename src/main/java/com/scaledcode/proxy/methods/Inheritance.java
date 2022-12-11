package com.scaledcode.proxy.methods;

import com.scaledcode.proxy.utils.FakePasswordHasher;

import java.util.HashMap;

public class Inheritance<K, V> extends HashMap<K, V> {
    @Override
    public V get(Object key) {
        long startTime = System.nanoTime();
        V returnValue = super.get(key);
        OneForOne.timeConsumed += (System.nanoTime() - startTime);
        return returnValue;
    }

    @Override
    public V put(K key, V value) {
        if (key instanceof String && value instanceof String && "password".equals(key.toString())) {
            return super.put((K) "hashedPassword", (V) FakePasswordHasher.hashPassword(value.toString()));
        }
        return super.put(key, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return true;
    }
}
