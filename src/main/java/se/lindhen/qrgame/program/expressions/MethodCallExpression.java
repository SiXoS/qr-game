package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.objects.ObjectValue;

import java.util.ArrayList;
import java.util.List;

public class MethodCallExpression extends Expression {

    private final Expression object;
    private final int methodId;
    private final List<Expression> arguments;

    public MethodCallExpression(Expression object, int method, List<Expression> arguments, Type returnType) {
        super(returnType, joinExpressions(object, arguments));
        this.object = object;
        this.methodId = method;
        this.arguments = arguments;
    }

    private static Expression[] joinExpressions(Expression object, List<Expression> arguments) {
        ArrayList<Expression> expressions = new ArrayList<>();
        expressions.add(object);
        expressions.addAll(arguments);
        return expressions.toArray(new Expression[0]);
    }

    @Override
    public Object calculate(Program program) {
        return ((ObjectValue) object.calculate(program)).execute(methodId, arguments, program);
    }

    public Expression getObjectExpression() {
        return object;
    }

    public List<Expression> getArgumentExpressions() {
        return arguments;
    }

    public int getMethodId() {
        return methodId;
    }
}
