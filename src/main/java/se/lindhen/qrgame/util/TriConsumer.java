package se.lindhen.qrgame.util;

@FunctionalInterface
public interface TriConsumer<A,B,C> {

    void apply(A first, B second, C third);

}
