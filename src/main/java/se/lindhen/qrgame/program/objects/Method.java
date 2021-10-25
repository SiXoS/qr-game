package se.lindhen.qrgame.program.objects;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.ResultOrInvalidation;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Method<O extends ObjectValue> {

    protected final String name;
    protected final FunctionType functionType;

    protected Method(String name, FunctionType functionType) {
        this.name = name;
        this.functionType = functionType;
    }

    public abstract Object execute(O object, List<Expression> arguments, Program program);

    public final ResultOrInvalidation<Type> validate(ObjectType objectType, List<Type> arguments, ParserRuleContext ctx) {
        ArrayList<Type> allArguments = new ArrayList<>();
        allArguments.add(objectType);
        allArguments.addAll(arguments);
        return functionType.validate(allArguments, ctx);
    }

    public String getName() {
        return name;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    /**
     * @deprecated Only used for backwards compatibility with the v1 bytecode
     */
    @Deprecated()
    public Type getReturnType(ObjectType objType) {
        GenericTypeTracker genericTypeTracker = new GenericTypeTracker();
        try {
            FunctionType fakeFuncType = new FunctionType(functionType.getReturnType(), Collections.singletonList(functionType.getFunctionParameters().get(0)));
            fakeFuncType.coerceParameters(Collections.singletonList(objType), genericTypeTracker);
            return functionType.getReturnType().inferFromGenerics(genericTypeTracker);
        } catch (CoercionException e) {
            throw new RuntimeException("Could not determine return type of method " + objType.getQgClass().getName() + "." + name, e);
        }
    }
}
