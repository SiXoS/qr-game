package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.Method;
import se.lindhen.qrgame.program.objects.ObjectValue;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.util.TriFunction;

import java.util.List;

public class LambdaMethod<O extends ObjectValue> extends Method<O> {

    private final TriFunction<O, List<Expression>, Program, Object> function;

    public LambdaMethod(String name, TriFunction<O, List<Expression>, Program, Object> function, FunctionType functionType) {
        super(name, functionType);
        this.function = function;
    }

    @Override
    public Object execute(O object, List<Expression> arguments, Program program) {
        return function.apply(object, arguments, program);
    }

    public String getName() {
        return name;
    }

}
