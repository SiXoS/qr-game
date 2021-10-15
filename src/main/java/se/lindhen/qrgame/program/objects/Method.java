package se.lindhen.qrgame.program.objects;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.ResultOrInvalidation;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.types.CoercionException;
import se.lindhen.qrgame.program.types.GenericTypeTracker;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Method<O extends ObjectValue> {

    protected final String name;
    protected final FunctionDeclaration functionDeclaration;

    protected Method(String name, FunctionDeclaration functionDeclaration) {
        this.name = name;
        this.functionDeclaration = functionDeclaration;
    }

    public abstract Object execute(O object, List<Expression> arguments, Program program);

    public final ResultOrInvalidation<Type> validate(ObjectType objectType, List<Type> arguments, ParserRuleContext ctx) {
        ArrayList<Type> allArguments = new ArrayList<>();
        allArguments.add(objectType);
        allArguments.addAll(arguments);
        return functionDeclaration.validate(allArguments, ctx);
    }

    public String getName() {
        return name;
    }

    public FunctionDeclaration getFunctionDeclaration() {
        return functionDeclaration;
    }

    /**
     * @deprecated Only used for backwards compatibility with the v1 bytecode
     */
    @Deprecated()
    public Type getReturnType(ObjectType objType) {
        GenericTypeTracker genericTypeTracker = new GenericTypeTracker(objType.getInnerTypes().size(), Collections.singletonList(functionDeclaration.getFunctionParameters().get(0)));
        try {
            genericTypeTracker.coerce(Collections.singletonList(objType));
            return functionDeclaration.getReturnType().inferFromGenerics(genericTypeTracker);
        } catch (CoercionException e) {
            throw new RuntimeException("Could not determine return type of method " + objType.getQgClass().getName() + "." + name, e);
        }
    }
}
