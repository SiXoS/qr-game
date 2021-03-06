package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.objects.TreeSetClass;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class TreeSetFromVarargFunction extends Function {

    public TreeSetFromVarargFunction() {
        super("treeSet", new FunctionType(
                TreeSetClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new VarargType(new GenericType(0).withConstraints(ComparableType.COMPARABLE_TYPE))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        TreeSet<Object> treeSet = new TreeSet<>();
        for (Expression argument : arguments) {
            treeSet.add(argument.calculate(program));
        }
        return TreeSetClass.getQgClass().createInstance(treeSet);
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
