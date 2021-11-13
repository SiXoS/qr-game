package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.Iterator;
import java.util.List;

public abstract class ObjectValue {

    protected final String name;

    public ObjectValue(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Object execute(int methodId, List<Expression> arguments, Program program);

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    public Iterator<Object> iterator() {
        return null;
    }
}
