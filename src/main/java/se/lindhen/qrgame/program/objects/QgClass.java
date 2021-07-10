package se.lindhen.qrgame.program.objects;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QgClass<O extends ObjectValue> {

    private final String name;
    protected final List<? extends Method<O>> methods;
    protected final Map<String, Integer> methodNameToIndex = new HashMap<>();
    private final int numTypeArguments;

    protected QgClass(String name, int numTypeArguments) {
        this.name = name;
        this.methods = getMethods();
        for (int i = 0; i < methods.size(); i++) {
            Integer prevValue = methodNameToIndex.put(methods.get(i).name, i);
            if (prevValue != null) {
                throw new IllegalArgumentException("Class " + name + " has two methods with name " + methods.get(i).name);
            }
        }
        this.numTypeArguments = numTypeArguments;
    }

    protected abstract List<? extends Method<O>> getMethods();

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj; // There should only be one class instance per class.
    }

    public Object execute(O object, int methodId, List<Expression> arguments, Program program) {
        return methods.get(methodId).execute(object, arguments, program);
    }

    public Type getReturnType(int methodId, ObjectType objectType) {
        return methods.get(methodId).getReturnType(objectType);
    }

    public ValidationResult validate(ParserRuleContext ctx, ObjectType objectType, String methodName, List<Expression> arguments) {
        Integer methodId = methodNameToIndex.get(methodName);
        if (methodId == null) {
            return ValidationResult.invalid(ctx, "Method " + methodName + " does not exist on class " + getName());
        } else {
            return methods.get(methodId).validate(objectType, arguments, ctx);
        }
    }

    public String getName() {
        return name;
    }

    public int getNumTypeArguments() {
        return numTypeArguments;
    }

    public Integer lookupMethodId(String methodName) {
        return methodNameToIndex.get(methodName);
    }

    public Method<O> getMethod(int id) {
        return methods.get(id);
    }

    public abstract boolean isIterable();

    public Type iteratorType(ObjectType objectType) {
        return null;
    }

    public abstract Type getObjectTypeFromTypeArgs(List<Type> typeArguments);

    public abstract boolean isComparable();
}
