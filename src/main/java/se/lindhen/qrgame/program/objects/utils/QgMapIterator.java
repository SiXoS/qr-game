package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.objects.MapEntryClass;
import se.lindhen.qrgame.program.types.Type;

import java.util.Iterator;
import java.util.Map;

public class QgMapIterator implements Iterator<Object> {

    private final Iterator<Map.Entry<Object, Object>> mapIterator;

    public QgMapIterator(Iterator<Map.Entry<Object, Object>> mapIterator) {
        this.mapIterator = mapIterator;
    }

    @Override
    public boolean hasNext() {
        return mapIterator.hasNext();
    }

    @Override
    public Object next() {
        Map.Entry<Object, Object> entry = mapIterator.next();
        return MapEntryClass.getQgClass().createInstance(entry.getKey(), entry.getValue());
    }
}
