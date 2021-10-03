package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.functions.LegacyFunction;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;
import se.lindhen.qrgame.program.types.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class LinkedListLegacyFunction extends Function implements LegacyFunction {

    public static final String NAME = "linkedList";

    public LinkedListLegacyFunction() {
        super(NAME, null);
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        LinkedList<Object> list = CollectionConstructionUtils.createCollection(arguments, program, size -> new LinkedList<>());
        return ListClass.getQgClass().createInstance(list);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Type getReturnType(List<Type> arguments) {
        return ListClass.getQgClass().getObjectType(arguments);
    }
}
