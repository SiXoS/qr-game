package se.lindhen.qrgame.program.types;

import java.util.HashMap;

public class GenericTypeTracker {
    private final HashMap<Integer, Type> genericTypes;

    public GenericTypeTracker() {
        genericTypes = new HashMap<>();
    }

    public Type coerce(int genericTypeId, Type type) throws CoercionException {
        if (!genericTypes.containsKey(genericTypeId)) {
            genericTypes.put(genericTypeId, type);
            return type;
        } else {
            Type existingType = genericTypes.get(genericTypeId);
            if (type.canBeAssignedTo(existingType)) {
                return existingType;
            } else {
                throw new CoercionException(existingType, type, genericTypeId);
            }
        }
    }

    public Type getInferredType(int genericTypeId) {
        return genericTypes.get(genericTypeId);
    }
}
