package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.HashSetClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class HashSetFromTypeFunction extends Function {

    public HashSetFromTypeFunction() {
        super("hashSet", new FunctionDeclaration(1,
                HashSetClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new TypeType(new GenericType(0))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return HashSetClass.getQgClass().createInstance(new HashSet<>());
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
