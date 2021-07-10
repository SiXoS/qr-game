package se.lindhen.qrgame.program.functions.datastructures;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.IndexedHashSetClass;
import se.lindhen.qrgame.util.IndexedHashSet;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;

import java.util.ArrayList;
import java.util.Optional;

public class IndexedHashSetFunction extends Function {

    public static final String NAME = "indexedHashSet";

    public IndexedHashSetFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return IndexedHashSetClass.getQgClass().getObjectType(arguments);
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        IndexedHashSet<Object> set = CollectionConstructionUtils.createCollection(arguments, program, size -> new IndexedHashSet<>());
        return IndexedHashSetClass.getQgClass().createInstance(CollectionConstructionUtils.innerTypeFromArguments(arguments), set);
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
