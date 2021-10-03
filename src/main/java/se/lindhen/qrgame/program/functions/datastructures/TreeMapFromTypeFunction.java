package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.TreeMapClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class TreeMapFromTypeFunction extends Function {

    public TreeMapFromTypeFunction() {
        super("treeMap", new FunctionDeclaration(2,
                TreeMapClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))),
                new TypeType(new GenericType(0)),
                new TypeType(new GenericType(1))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return TreeMapClass.getQgClass().createInstance(new TreeMap<>());
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(2);
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
