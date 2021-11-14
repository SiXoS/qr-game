package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.Iterator;
import java.util.List;

public abstract class ObjectValue {

    public abstract Object execute(int methodId, List<Expression> arguments, Program program);

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    public Iterator<Object> iterator() {
        return null;
    }
}
