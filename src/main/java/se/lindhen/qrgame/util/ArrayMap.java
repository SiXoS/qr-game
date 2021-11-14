package se.lindhen.qrgame.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ArrayMap<V> {

    private final ArrayList<V> list = new ArrayList<>();

    public V get(int key) {
        if (list.size() <= key) {
            return null;
        }
        return list.get(key);
    }

    public V put(int key, V value) {
        ensureSizeIncludesKey(key);
        return list.set(key, value);
    }

    private void ensureSizeIncludesKey(int key) {
        if (list.size() <= key) {
            for (int i = 0; i <= key; i++) {
                list.add(null);
            }
        }
    }

    public V remove(int key) {
        V toReturn = get(key);
        if (toReturn != null) {
            list.set(key, null);
        }
        return toReturn;
    }

    public void clear() {
        list.clear();
    }

    public Collection<V> values() {
        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void reset(int from, int to) {
        for (int i = from; i < to; i++) {
            list.set(i, null);
        }
    }
}
