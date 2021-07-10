package se.lindhen.qrgame.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ArrayMap<V> {

    private final ArrayList<V> list = new ArrayList<>();
    private int size = 0;

    public int size() {
        return size;
    }

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
        size = 0;
    }

    public Collection<V> values() {
        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void forEach(BiConsumer<Integer, V> consumer) {
        for (int i = 0; i < list.size(); i++) {
            V value = list.get(i);
            if (value != null) {
                consumer.accept(i, value);
            }
        }
    }

}
