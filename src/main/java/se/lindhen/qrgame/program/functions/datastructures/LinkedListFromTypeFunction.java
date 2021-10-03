package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class LinkedListFromTypeFunction extends Function {

    public LinkedListFromTypeFunction() {
        super("linkedList", new FunctionDeclaration(1,
                ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new TypeType(new GenericType(0))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return ListClass.getQgClass().createInstance(new LinkedList<>());
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(1);
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
