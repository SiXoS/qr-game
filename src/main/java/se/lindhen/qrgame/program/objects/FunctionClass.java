package se.lindhen.qrgame.program.objects;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;

import java.util.*;

public class FunctionClass extends QgClass<FunctionClass.FunctionReferenceValue> {

    private static final String NAME = "Function";
    private static final FunctionClass qgClass = new FunctionClass();

    protected FunctionClass() {
        super(NAME);
    }

    @Override
    protected List<? extends Method<FunctionReferenceValue>> getMethods() {
        return Collections.singletonList(new InvocationMethod());
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.valid();
    }

    @Override
    public boolean isIterable() {
        return false;
    }

    @Override
    public Type getObjectTypeFromTypeArgs(List<Type> typeArguments) {
        return new ObjectType(this, typeArguments.toArray(new Type[0]));
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    private class InvocationMethod extends Method<FunctionReferenceValue> {

        public static final String NAME = "invoke";

        protected InvocationMethod() {
            super(NAME);
        }

        @Override
        public Object execute(FunctionReferenceValue object, List<Expression> arguments, Program program) {
            return object.invoke(arguments, program);
        }

        @Override
        public ValidationResult validate(ObjectType objectType, List<Expression> arguments, ParserRuleContext ctx) {
            return validateArguments(arguments, ctx, objectType.getInnerTypes().subList(0, objectType.getInnerTypes().size() - 1));
        }

        @Override
        public Type getReturnType(ObjectType objectType) {
            return objectType.getInnerTypes().get(objectType.getInnerTypes().size() - 1);
        }

        @Override
        public Optional<Integer> getConstantParameterCount() {
            return Optional.empty();
        }
    }

    public class FunctionReferenceValue extends ObjectValue {

        private final Function function;

        public FunctionReferenceValue(Function function) {
            super(NAME);
            this.function = function;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return FunctionClass.this.execute(this, methodId, arguments, program);
        }

        public Object invoke(List<Expression> arguments, Program program) {
            return function.execute(arguments, program);
        }

        protected Function getFunction() {
            return function;
        }
    }
}
