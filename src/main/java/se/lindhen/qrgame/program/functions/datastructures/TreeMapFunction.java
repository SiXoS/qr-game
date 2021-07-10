package se.lindhen.qrgame.program.functions.datastructures;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.utils.MapConstructionUtils;
import se.lindhen.qrgame.program.objects.TreeMapClass;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

public class TreeMapFunction extends Function {

    private static final String NAME = "treeMap";

    public TreeMapFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return MapConstructionUtils.typeFromArguments(TreeMapClass.getQgClass(), arguments);
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        TreeMap<Object, Object> map = MapConstructionUtils.createMap(arguments, program, TreeMap::new);
        return TreeMapClass.getQgClass().createInstance(MapConstructionUtils.typeFromArguments(TreeMapClass.getQgClass(), arguments), map);
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        ValidationResult validationResult = MapConstructionUtils.validate(arguments, ctx);
        if (validationResult.isValid()) {
            Type keyType = MapConstructionUtils.typeFromArguments(TreeMapClass.getQgClass(), arguments).getInnerTypes().get(0);
            if (!keyType.isComparable()) {
                validationResult = ValidationResult.invalid(ctx, "Tree map requires the key is sortable. Specified key type '" + keyType + "' is not sortable.");
            }
        }
        return validationResult;
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }
}
