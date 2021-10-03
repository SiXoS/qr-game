package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.TypeType;
import se.lindhen.qrgame.program.types.VarargType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ArrayListFromVarargFunction extends Function {

    public ArrayListFromVarargFunction() {
        super("arrayList", new FunctionDeclaration(1,
                ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new VarargType(new GenericType(0))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        ArrayList<Object> arrayList = new ArrayList<>(arguments.size());
        for (Expression argument : arguments) {
            arrayList.add(argument.calculate(program));
        }
        return ListClass.getQgClass().createInstance(arrayList);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }

    @Override
    public boolean isConstant() {
        return true;
    }

}
