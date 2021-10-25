package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.objects.IndexedHashSetClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.TypeType;
import se.lindhen.qrgame.program.types.VarargType;
import se.lindhen.qrgame.util.IndexedHashSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class IndexedHashSetFromVarargFunction extends Function {

    public IndexedHashSetFromVarargFunction() {
        super("indexedHashSet", new FunctionType(
                IndexedHashSetClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new VarargType(new GenericType(0))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        IndexedHashSet<Object> hashSet = new IndexedHashSet<>();
        for (Expression argument : arguments) {
            hashSet.add(argument.calculate(program));
        }
        return IndexedHashSetClass.getQgClass().createInstance(hashSet);
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
