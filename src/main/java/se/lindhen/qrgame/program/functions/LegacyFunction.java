package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.types.Type;

import java.util.List;

public interface LegacyFunction {

    Type getReturnType(List<Type> arguments);

}
