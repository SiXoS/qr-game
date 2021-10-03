package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.VarargType;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class LinkedListFromVarargFunction extends Function {

    public LinkedListFromVarargFunction() {
        super("linkedList", new FunctionDeclaration(1,
                ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new VarargType(new GenericType(0))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        LinkedList<Object> linkedList = new LinkedList<>();
        for (Expression argument : arguments) {
            linkedList.add(argument.calculate(program));
        }
        return ListClass.getQgClass().createInstance(linkedList);
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
