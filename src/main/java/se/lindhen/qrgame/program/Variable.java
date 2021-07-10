package se.lindhen.qrgame.program;

import se.lindhen.qrgame.program.types.Type;

public class Variable {

    private final int id;
    private final Type type;
    private final boolean onStack;

    public Variable(int id, Type type, boolean onStack) {
        this.id = id;
        this.type = type;
        this.onStack = onStack;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public boolean isOnStack() {
        return onStack;
    }
}
