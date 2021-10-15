package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.ResultOrInvalidation;
import se.lindhen.qrgame.program.types.CoercionException;
import se.lindhen.qrgame.program.types.GenericTypeTracker;
import se.lindhen.qrgame.program.types.Type;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FunctionDeclaration {

    private final int typeParameters;
    private final Type returnType;
    private final List<Type> functionParameters;

    public FunctionDeclaration(int typeParameters, Type returnType, List<Type> functionParameters) {
        this.typeParameters = typeParameters;
        this.returnType = returnType;
        this.functionParameters = functionParameters;
        assert functionParameters.stream().limit(Math.max(functionParameters.size() - 1, 0)).noneMatch(Type::isVararg): "Only the last type parameter can be vararg";
    }

    public FunctionDeclaration(int typeParameters, Type returnType, Type... functionParameters) {
        this(typeParameters, returnType, Arrays.asList(functionParameters));
    }

    public ResultOrInvalidation<Type> validate(List<Type> functionCallParameters, ParserRuleContext ctx) {
        GenericTypeTracker genericTypeTracker = new GenericTypeTracker(typeParameters, functionParameters);
        try {
            List<Type> actualTypes = genericTypeTracker.coerce(functionCallParameters);
            if (actualTypes.size() != functionParameters.size()) {
                return ResultOrInvalidation.invalid(ValidationResult.invalid(ctx, "Expected " + functionParameters.size() + " arguments but got " + actualTypes.size()));
            }
            for (int i = 0; i < functionParameters.size(); i++) {
                if (!functionParameters.get(i).acceptsType(actualTypes.get(i))) {
                    return ResultOrInvalidation.invalid(ValidationResult.invalid(ctx, "Can not assign a value of type " + actualTypes.get(i) + " to parameter with type " + functionParameters.get(i)));
                }
            }
            return ResultOrInvalidation.valid(returnType.inferFromGenerics(genericTypeTracker));
        } catch (CoercionException e) {
            return ResultOrInvalidation.invalid(ValidationResult.invalid(ctx,
                    "Could not resolve type parameters for generic type with id " + e.genericTypeId + ". " +
                    "Tried to assign both " + e.preExistingType + " and " + e.newType + " which are not compatible. " +
                    "For function declaration: " + this));
        }
    }

    public boolean returnsVoid() {
        return returnType.isVoid();
    }

    public Optional<Integer> getConstantParameterCount() {
        if (functionParameters.isEmpty()) return Optional.of(0);
        if (functionParameters.get(functionParameters.size() - 1).isVararg()) return Optional.empty();
        return Optional.of(functionParameters.size());
    }

    /**
     *
     * @return The low level argument count. vararg is counted as one argument as it is represented as a list.
     */
    public int numberOfTrueArguments() {
        return functionParameters.size();
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<Type> getFunctionParameters() {
        return functionParameters;
    }

    @Override
    public String toString() {
        return "(" + functionParameters.stream().map(Object::toString).collect(Collectors.joining(", ")) + "): " + returnType;
    }
}
