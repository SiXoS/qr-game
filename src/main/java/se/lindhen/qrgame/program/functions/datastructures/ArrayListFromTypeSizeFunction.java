package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ArrayListFromTypeSizeFunction extends Function {

    public ArrayListFromTypeSizeFunction() {
        super("arrayList", new FunctionType(
                ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new TypeType(new GenericType(0)),
                NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return ListClass.getQgClass().createInstance(new ArrayList<>((int)(double) arguments.get(1).calculate(program)));
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
