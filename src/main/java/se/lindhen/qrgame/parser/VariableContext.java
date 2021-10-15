package se.lindhen.qrgame.parser;

import se.lindhen.qrgame.program.Variable;

import java.util.HashMap;

public class VariableContext {

    public int currentVariableIndex = 0;
    public final HashMap<String, Variable> variables = new HashMap<>();
    public final boolean onStack;

    public VariableContext(boolean onStack) {
        this.onStack = onStack;
    }
}
