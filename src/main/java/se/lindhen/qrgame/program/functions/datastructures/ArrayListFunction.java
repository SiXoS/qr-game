package se.lindhen.qrgame.program.functions.datastructures;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;

import java.util.ArrayList;
import java.util.Optional;

public class ArrayListFunction extends Function {

    public static final String NAME = "arrayList";

    public ArrayListFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return ListClass.getQgClass().getObjectType(arguments);
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        ArrayList<Object> list = CollectionConstructionUtils.createCollection(arguments, program, ArrayList::new);
        return ListClass.getQgClass().createInstance(CollectionConstructionUtils.innerTypeFromArguments(arguments), list);
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return CollectionConstructionUtils.validateCollection(arguments, ctx);
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
