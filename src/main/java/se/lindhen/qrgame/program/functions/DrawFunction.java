package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.objects.ShapeClass;
import se.lindhen.qrgame.program.types.VoidType;

import java.util.ArrayList;
import java.util.Optional;

public class DrawFunction extends Function {

    public static final String NAME = "draw";

    public DrawFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return VoidType.VOID_TYPE;
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        program.addDrawing(((ShapeClass.ShapeObject) arguments.get(0).calculate(program)).getShape());
        return null;
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx, new ObjectType(ShapeClass.getQgClass()));
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(1);
    }
}
