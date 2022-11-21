package com.scaledcode.proxy.methods;

import com.scaledcode.proxy.utils.FakePasswordHasher;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


@RequiredArgsConstructor
public class OneForOne<K, V> implements Map<K, V> {
    public static long timeConsumed;
    private final Map<K, V> wrapped;

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return wrapped.containsValue(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return wrapped.containsValue(value);
    }

    @Override
    public V get(Object key) {
        long startTime = System.nanoTime();
        V returnValue = wrapped.get(key);
        OneForOne.timeConsumed += (System.nanoTime() - startTime);
        return returnValue;
    }

    @Override
    public V put(K key, V value) {
        if (key instanceof String && value instanceof String && "password".equals(key.toString())) {
            return wrapped.put((K) "hashedPassword", (V) FakePasswordHasher.hashPassword(value.toString()));
        }
        return wrapped.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return wrapped.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        wrapped.putAll(m);
    }

    @Override
    public void clear() {
        wrapped.clear();
    }

    @Override
    public Set<K> keySet() {
        return wrapped.keySet();
    }

    @Override
    public Collection<V> values() {
        return wrapped.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return wrapped.entrySet();
    }
}
