package se.lindhen.qrgame.util;

@FunctionalInterface
public interface TriFunction<A,B,C,R> {

    R apply(A first, B second, C third);

}
