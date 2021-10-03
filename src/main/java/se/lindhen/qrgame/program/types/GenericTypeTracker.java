package se.lindhen.qrgame.program.types;

import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.ListClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericTypeTracker {
    private final Type[] genericType;
    private final ArrayList<Type> functionParameterTypes;
    private final VarargType vararg;

    public GenericTypeTracker(int numGenericTypes, List<Type> functionParameterTypes) {
        genericType = new Type[numGenericTypes];
        if (!functionParameterTypes.isEmpty() && functionParameterTypes.get(functionParameterTypes.size() - 1).isVararg()) {
            this.vararg = (VarargType) functionParameterTypes.get(functionParameterTypes.size() - 1);
            this.functionParameterTypes = new ArrayList<>(functionParameterTypes.subList(0, functionParameterTypes.size() - 1));
        } else {
            this.vararg = null;
            this.functionParameterTypes = new ArrayList<>(functionParameterTypes);
        }
    }

    public List<Type> coerce(List<Type> functionCallParameterTypes) {
        assert functionCallParameterTypes.size() >= functionParameterTypes.size();
        ArrayList<Type> actualTypes = new ArrayList<>();
        for (int i = 0; i < functionParameterTypes.size(); i++) {
            actualTypes.add(functionParameterTypes.get(i).coerce(functionCallParameterTypes.get(i), this));
        }
        if (vararg != null) {
            Type coerceBasis = VoidType.VOID_TYPE;
            if(functionCallParameterTypes.size() > functionParameterTypes.size()) {
                coerceBasis = functionCallParameterTypes.get(functionParameterTypes.size());
            }
            VarargType actualVarargType = (VarargType) vararg.coerce(coerceBasis, this);
            actualTypes.add(actualVarargType);
        }
        return actualTypes;
    }

    public Type coerce(int genericTypeId, Type type) {
        assert genericType.length > genericTypeId;
        Type existingType = genericType[genericTypeId];
        if (existingType == null) {
            genericType[genericTypeId] = type;
            return type;
        } else {
            if (type.canBeAssignedTo(existingType)) {
                return existingType;
            } else {
                throw new CoercionException(existingType, type, genericTypeId);
            }
        }
    }

    public Type getInferredType(int genericTypeId) {
        assert genericType.length > genericTypeId;
        return genericType[genericTypeId];
    }
}
