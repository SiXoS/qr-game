package se.lindhen.qrgame.program.functions.datastructures;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.HashMapClass;
import se.lindhen.qrgame.program.objects.utils.MapConstructionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class HashMapFunction extends Function {

    private static final String NAME = "hashMap";

    public HashMapFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return MapConstructionUtils.typeFromArguments(HashMapClass.getQgClass(), arguments);
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        HashMap<Object, Object> map = MapConstructionUtils.createMap(arguments, program, HashMap::new);
        return HashMapClass.getQgClass().createInstance(MapConstructionUtils.typeFromArguments(HashMapClass.getQgClass(), arguments), map);
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return MapConstructionUtils.validate(arguments, ctx);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }
}
