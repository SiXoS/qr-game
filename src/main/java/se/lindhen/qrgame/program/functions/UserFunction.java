package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.Variable;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.statements.Statement;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserFunction extends Function {

    private final int id;
    private final int numberOfTrueArguments;
    private final Optional<Integer> constantArgumentCount;
    private final Statement body;
    private final int numVariables;
    private final int minimumArguments;

    public UserFunction(int id, FunctionType functionType, int numVariables, Statement body) {
        this(id, functionType.getFunctionParameters().size(), functionType.getConstantParameterCount(), numVariables, body, functionType);
    }

    public UserFunction(int id, int numberOfTrueArguments, Optional<Integer> constantArgumentCount, int numVariables, Statement body) {
        this(id, numberOfTrueArguments, constantArgumentCount, numVariables, body, null);
    }

    private UserFunction(int id, int numberOfTrueArguments, Optional<Integer> constantArgumentCount, int numVariables, Statement body, FunctionType functionType) {
        super("_user_function_" + id, functionType);
        this.id = id;
        this.numberOfTrueArguments = numberOfTrueArguments;
        this.constantArgumentCount = constantArgumentCount;
        this.minimumArguments = constantArgumentCount.orElse(numberOfTrueArguments - 1);
        this.body = body;
        this.numVariables = numVariables;
    }

    public int getId() {
        return id;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        Object[] parameters = new Object[numberOfTrueArguments];
        for (int i = 0; i < minimumArguments; i++) {
            parameters[i] = arguments.get(i).calculate(program);
        }
        if (!constantArgumentCount.isPresent()) { // Function has a vararg parameter
            ArrayList<Object> values = new ArrayList<>();
            for (int i = minimumArguments; i < arguments.size(); i++) {
                values.add(arguments.get(i).calculate(program));
            }
            parameters[numberOfTrueArguments - 1] = ListClass.getQgClass().createInstance(values);
        }
        int stackResetOffset = program.enterStackContext(numVariables);
        for (int i = 0; i < parameters.length; i++) {
            program.setStackVariable(i, parameters[i]);
        }
        body.run(program);
        Object returnValue = program.getReturnValueAndResume();
        program.leaveStackContext(stackResetOffset);
        return returnValue;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return constantArgumentCount;
    }

    public int getNumberOfTrueArguments() {
        return numberOfTrueArguments;
    }

    public int getNumVariables() {
        return numVariables;
    }

}
