package se.lindhen.qrgame.bytecode;

import se.lindhen.qrgame.program.types.Type;

import java.util.HashMap;

public enum TypeCode {
    NUMBER(0),
    BOOL(1),
    OBJECT(2),
    STRUCT(3);

    private final int code;
    private static final HashMap<Type.BaseType, TypeCode> typeMapping = new HashMap<Type.BaseType, TypeCode>() {{
        put(Type.BaseType.NUMBER, NUMBER);
        put(Type.BaseType.BOOL, BOOL);
        put(Type.BaseType.STRUCT, STRUCT);
        put(Type.BaseType.OBJECT, OBJECT);
    }};

    TypeCode(int code){
        this.code = code;
    }

    public static TypeCode fromType(Type type) {
        return typeMapping.get(type.getBaseType());
    }

    public static TypeCode fromCode(int code) {
        for (TypeCode value : TypeCode.values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("No type code with id " + code);
    }

    public int getCode() {
        return code;
    }
}
