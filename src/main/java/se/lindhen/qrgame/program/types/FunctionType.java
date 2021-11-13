package se.lindhen.qrgame.program.types;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.ResultOrInvalidation;

import java.util.*;
import java.util.stream.Collectors;

public class FunctionType extends Type {

    private final Type returnType;
    private final List<Type> functionParameters;

    public FunctionType(Type returnType, List<Type> functionParameters) {
        super(BaseType.FUNCTION);
        this.returnType = returnType;
        this.functionParameters = functionParameters;
        assert functionParameters.stream().limit(Math.max(functionParameters.size() - 1, 0)).noneMatch(Type::isVararg): "Only the last type parameter can be vararg";
    }

    public FunctionType(Type returnType, Type... functionParameters) {
        this(returnType, Arrays.asList(functionParameters));
    }

    @Override
    public String toString() {
        return "(" + functionParameters.stream().map(Object::toString).collect(Collectors.joining(", ")) + "): " + returnType;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        if (!sourceType.isFunction()) return false;
        FunctionType functionType = (FunctionType) sourceType;
        if (!getReturnType().acceptsType(functionType.getReturnType())) {
            return false;
        }
        return acceptsParameters(functionType.functionParameters);
    }

    public boolean acceptsParameters(List<Type> callParameters) {
        if (functionParameters.isEmpty()) {
            return callParameters.isEmpty();
        }
        int lastParameterIndex = functionParameters.size() - 1;
        Type lastParameter = functionParameters.get(lastParameterIndex);
        int minimumFuncParameters = lastParameter.isVararg() ? functionParameters.size() - 1 : functionParameters.size();
        if (callParameters.size() < minimumFuncParameters) return false;
        for (int i = 0; i < functionParameters.size() - 1; i++) {
            if(!functionParameters.get(i).acceptsType(callParameters.get(i))) {
                return false;
            }
        }
        if (lastParameter.isVararg()) {
            for (int i = lastParameterIndex; i < callParameters.size(); i++) {
                if (!lastParameter.acceptsType(callParameters.get(i))) return false;
            }
        } else {
            if (callParameters.size() != functionParameters.size()) return false;
            if (!lastParameter.acceptsType(callParameters.get(lastParameterIndex))) return false;
        }
        return true;
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) throws CoercionException {
        if (!type.isFunction()) return (Type) type.clone();
        FunctionType functionType = ((FunctionType) type);

        ArrayList<Type> actualTypes = coerceParameters(functionType.functionParameters, genericTypeTracker);
        Type returnType = this.returnType.coerce(functionType.returnType, genericTypeTracker);

        FunctionType resultingFunctionType = new FunctionType(returnType, actualTypes);
        ensureConsecutiveGenericIds(resultingFunctionType);
        return resultingFunctionType;
    }

    public ArrayList<Type> coerceParameters(List<Type> otherFunctionParameters, GenericTypeTracker genericTypeTracker) throws CoercionException {
        VarargSeparatedParams varargSeparatedParams = new VarargSeparatedParams(functionParameters);
        List<Type> nonVarargParams = varargSeparatedParams.getNonVarargParams();
        VarargType vararg = varargSeparatedParams.getVararg();

        assert otherFunctionParameters.size() >= nonVarargParams.size();

        ArrayList<Type> actualTypes = new ArrayList<>();
        for (int i = 0; i < nonVarargParams.size(); i++) {
            actualTypes.add(nonVarargParams.get(i).coerce(otherFunctionParameters.get(i), genericTypeTracker));
        }
        if (vararg != null) {
            actualTypes.add(getVarargType(otherFunctionParameters, vararg, nonVarargParams.size(), genericTypeTracker));
        }
        return actualTypes;
    }

    private void ensureConsecutiveGenericIds(FunctionType resultingFunctionType) {
        TreeSet<Integer> unresolvedGenerics = resultingFunctionType.getUnresolvedGenerics();
        if (!unresolvedGenerics.isEmpty()) {
            HashMap<Integer, Integer> genericRemapping = new HashMap<>();
            int currentGeneric = 0;
            for (Integer unresolvedGeneric : unresolvedGenerics) {
                genericRemapping.put(unresolvedGeneric, currentGeneric++);
            }
            resultingFunctionType.remapGenerics(genericRemapping);
        }
    }

    private VarargType getVarargType(List<Type> otherFunctionParameter, VarargType vararg, int varargIndex, GenericTypeTracker genericTypeTracker) throws CoercionException {
        VarargType actualVarargType;
        if (otherFunctionParameter.size() > varargIndex) {
            actualVarargType = (VarargType) vararg.coerce(otherFunctionParameter.get(varargIndex), genericTypeTracker);
        } else {
            actualVarargType = (VarargType) vararg.inferFromGenerics(genericTypeTracker);
        }
        for (int i = varargIndex + 1; i < otherFunctionParameter.size(); i++) {
            vararg.coerce(otherFunctionParameter.get(i), genericTypeTracker);
        }
        return actualVarargType;
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return new FunctionType(
                returnType.inferFromGenerics(genericTypeTracker),
                functionParameters.stream()
                        .map(param -> param.inferFromGenerics(genericTypeTracker))
                        .collect(Collectors.toList()));
    }

    @Override
    protected void getUnresolvedGenerics(Set<Integer> accumulator) {
        returnType.getUnresolvedGenerics(accumulator);
        functionParameters.forEach(param -> param.getUnresolvedGenerics(accumulator));
    }

    @Override
    protected void remapGenerics(Map<Integer, Integer> genericRemapping) {
        returnType.remapGenerics(genericRemapping);
        functionParameters.forEach(param -> param.remapGenerics(genericRemapping));
    }

    @Override
    protected Object clone() {
        return new FunctionType((Type) returnType.clone(), functionParameters.stream().map(type -> (Type) type.clone()).collect(Collectors.toList()));
    }

    public ResultOrInvalidation<Type> validate(List<Type> functionCallParameters, ParserRuleContext ctx) {
        return validate(functionCallParameters, Collections.emptyList(), ctx);
    }

    public ResultOrInvalidation<Type> validate(List<Type> functionCallParameters, List<Type> genericTip, ParserRuleContext ctx) {
        GenericTypeTracker genericTypeTracker = new GenericTypeTracker();
        try {
            for (int i = 0; i < genericTip.size(); i++) {
                genericTypeTracker.coerce(i, genericTip.get(i));
            }
            List<Type> actualTypes = coerceParameters(functionCallParameters, genericTypeTracker);
            if (actualTypes.size() != functionParameters.size()) {
                return ResultOrInvalidation.invalid(ValidationResult.invalid(ctx, "Expected " + functionParameters.size() + " arguments but got " + actualTypes.size()));
            }
            for (int i = 0; i < functionParameters.size(); i++) {
                if (!functionParameters.get(i).acceptsType(actualTypes.get(i))) {
                    return ResultOrInvalidation.invalid(ValidationResult.invalid(ctx, "Can not assign a value of type " + actualTypes.get(i) + " to parameter with type " + functionParameters.get(i)));
                }
            }
            if (actualTypes.stream().allMatch(type -> type.getUnresolvedGenerics().isEmpty())) {
                return ResultOrInvalidation.valid(returnType.inferFromGenerics(genericTypeTracker));
            } else {
                return ResultOrInvalidation.invalid(ValidationResult.invalid(ctx, "Could not infer all generic parameters in declaration: " + this));
            }
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

    public Type getReturnType() {
        return returnType;
    }

    public List<Type> getFunctionParameters() {
        return functionParameters;
    }

    private static class VarargSeparatedParams {
        private final List<Type> nonVarargParams;
        private final VarargType vararg;

        public List<Type> getNonVarargParams() {
            return nonVarargParams;
        }

        public VarargType getVararg() {
            return vararg;
        }

        public VarargSeparatedParams(List<Type> functionParameters) {
            if (!functionParameters.isEmpty() && functionParameters.get(functionParameters.size() - 1).isVararg()) {
                vararg = (VarargType) functionParameters.get(functionParameters.size() - 1);
                nonVarargParams = new ArrayList<>(functionParameters.subList(0, functionParameters.size() - 1));
            } else {
                vararg = null;
                nonVarargParams = new ArrayList<>(functionParameters);
            }
        }
    }
}
