package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.Iterator;
import java.util.List;

public abstract class ObjectValue {

    protected final String name;
    protected final ObjectType type;

    public ObjectValue(String name, ObjectType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ObjectType getType() {
        return type;
    }

    public abstract Object execute(int methodId, List<Expression> arguments, Program program);

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Hash code not implemented for " + getClass());
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("Equals not implemented for " + getClass());
    }

    public Iterator<Object> iterator() {
        return null;
    }
}
