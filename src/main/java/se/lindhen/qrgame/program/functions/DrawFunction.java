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
import java.util.List;
import java.util.Optional;

public class DrawFunction extends Function {

    public static final String NAME = "draw";

    public DrawFunction() {
        super(NAME, new FunctionDeclaration(0, VoidType.VOID_TYPE, new ObjectType(ShapeClass.getQgClass())));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        program.addDrawing(((ShapeClass.ShapeObject) arguments.get(0).calculate(program)).getShape());
        return null;
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(1);
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
