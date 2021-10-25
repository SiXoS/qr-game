package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;
import java.util.List;

public class FunctionReferenceInvocationExpression extends Expression {

    private final Expression functionReference;
    private final List<Expression> arguments;

    public FunctionReferenceInvocationExpression(Expression functionReference, List<Expression> arguments, Type returnType) {
        super(returnType, joinExpressions(functionReference, arguments));
        this.functionReference = functionReference;
        this.arguments = arguments;
    }

    private static Expression[] joinExpressions(Expression functionReference, List<Expression> arguments) {
        ArrayList<Expression> expressions = new ArrayList<>();
        expressions.add(functionReference);
        expressions.addAll(arguments);
        return expressions.toArray(new Expression[0]);
    }

    @Override
    public Object calculate(Program program) {
        return ((Function) functionReference.calculate(program)).execute(arguments, program);
    }
}
