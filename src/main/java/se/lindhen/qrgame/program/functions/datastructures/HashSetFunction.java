package se.lindhen.qrgame.program.functions.datastructures;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.HashSetClass;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class HashSetFunction extends Function {

    public static final String NAME = "hashSet";

    public HashSetFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return HashSetClass.getQgClass().getObjectType(arguments);
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        HashSet<Object> set = CollectionConstructionUtils.createCollection(arguments, program, HashSet::new);
        return HashSetClass.getQgClass().createInstance(CollectionConstructionUtils.innerTypeFromArguments(arguments), set);
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return CollectionConstructionUtils.validateCollection(arguments, ctx);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }
}
