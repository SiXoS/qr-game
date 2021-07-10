package se.lindhen.qrgame.util;

import java.util.*;

public class IndexedHashSet<E> extends AbstractSet<E> {

    List<E> dta = new ArrayList<>();
    Map<E, Integer> idx = new HashMap<>();

    public IndexedHashSet() {
    }

    public IndexedHashSet(Collection<E> items) {
        addAll(items);
    }

    @Override
    public boolean add(E item) {
        if (idx.containsKey(item)) {
            return false;
        }
        idx.put(item, dta.size());
        dta.add(item);
        return true;
    }

    /**
     * Remove and return an arbitrary item
     * @param id arbitrary position. Has to be less than size.
     */
    public E removeAt(int id) {
        if (id >= dta.size()) {
            return null;
        }
        E res = dta.get(id);
        idx.remove(res);
        E last = dta.remove(dta.size() - 1);
        // skip filling the hole if last is removed
        if (id < dta.size()) {
            idx.put(last, id);
            dta.set(id, last);
        }
        return res;
    }

    @Override
    public boolean remove(Object item) {
        Integer id = idx.get(item);
        if (id == null) {
            return false;
        }
        removeAt(id);
        return true;
    }

    /**
     * Retrieve an arbitrary item
     * @param id An arbitrary position. Has to be less than size.
     */
    public E get(int id) {
        return dta.get(id);
    }

    @Override
    public int size() {
        return dta.size();
    }

    @Override
    public Iterator<E> iterator() {
        return new RandomableHashSetIterator();
    }

    @Override
    public boolean contains(Object o) {
        return idx.containsKey(o);
    }

    @Override
    public void clear() {
        idx.clear();
        dta.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IndexedHashSet<?> that = (IndexedHashSet<?>) o;
        return dta.equals(that.dta) &&
                idx.equals(that.idx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dta, idx);
    }

    private class RandomableHashSetIterator implements Iterator<E> {

        private final Iterator<E> dtaIterator = dta.iterator();

        @Override
        public boolean hasNext() {
            return dtaIterator.hasNext();
        }

        @Override
        public E next() {
            return dtaIterator.next();
        }
    }
}