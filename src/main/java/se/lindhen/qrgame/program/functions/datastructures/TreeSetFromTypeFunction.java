package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.TreeSetClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class TreeSetFromTypeFunction extends Function {

    public TreeSetFromTypeFunction() {
        super("treeSet", new FunctionDeclaration(1,
                TreeSetClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new TypeType(new GenericType(0))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return TreeSetClass.getQgClass().createInstance(new TreeSet<>());
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