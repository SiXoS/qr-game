package se.lindhen.qrgame.program.objects.utils;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.GenericType;
import se.lindhen.qrgame.program.objects.Method;
import se.lindhen.qrgame.program.objects.ObjectValue;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.util.TriFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LambdaMethod<O extends ObjectValue, G extends GenericType> extends Method<O> {

    private final GenericType genericType;
    private final TriFunction<O, List<Expression>, Program, Object> function;
    private final List<G> types;

    public LambdaMethod(G returnType, String name, TriFunction<O, List<Expression>, Program, Object> function, G... argumentTypes) {
        super(name);
        this.function = function;
        this.types = Arrays.stream(argumentTypes).collect(Collectors.toList());
        this.genericType = returnType;
    }

    @Override
    public Object execute(O object, List<Expression> arguments, Program program) {
        return function.apply(object, arguments, program);
    }

    @Override
    public ValidationResult validate(ObjectType objectType, List<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx, types.stream().map(g -> g.getType(objectType)).collect(Collectors.toList()));
    }

    @Override
    public Type getReturnType(ObjectType objectType) {
        return genericType.getType(objectType);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(types.size());
    }

    public String getName() {
        return name;
    }

}
