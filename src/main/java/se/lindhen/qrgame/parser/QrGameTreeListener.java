package se.lindhen.qrgame.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.QrGameBaseListener;
import se.lindhen.qrgame.QrGameParser;
import se.lindhen.qrgame.program.*;
import se.lindhen.qrgame.program.functions.UserFunction;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.statements.ForEachStatement;
import se.lindhen.qrgame.program.statements.IfStatement;
import se.lindhen.qrgame.program.statements.WhileStatement;
import se.lindhen.qrgame.program.expressions.*;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.QgClass;
import se.lindhen.qrgame.program.statements.*;
import se.lindhen.qrgame.program.types.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QrGameTreeListener extends QrGameBaseListener {

    private int currentFunctionIndex = 0;
    private int currentLabelIndex = 0;
    private int currentStructId = 0;
    private final LinkedList<VariableContext> variableStack = new LinkedList<>();
    private final LinkedList<ArrayList<Statement>> blockStack = new LinkedList<>();
    private final LinkedList<WhenBuilder> whenStack = new LinkedList<>();
    private final HashMap<String, Struct> structsByName = new HashMap<>();
    private final HashMap<Integer, Struct> structsById = new HashMap<>();
    private final PredefinedFunctions functionMap = new PredefinedFunctions();
    private final HashMap<String, UserFunctionDeclaration> userDeclaredFunctions = new HashMap<>();
    private Optional<Type> activeReturnType = Optional.empty();
    private final HashMap<String, QgClass<?>> classByName = new HashMap<>();
    private final Program program;
    private HashMap<String, Integer> optimizedVariableIds;
    private final LinkedList<Expression> expressionStack = new LinkedList<>();
    private final ArrayList<Validator> validators = new ArrayList<>();
    private final HashMap<String, Integer> labelMap = new HashMap<>();
    private final LinkedList<String> labelStack = new LinkedList<>();
    private final HashMap<String, Expression> constants = new HashMap<>();
    private boolean definingConstant = false;
    private int loopCounter = 0;

    public QrGameTreeListener(Program program, HashMap<String, Integer> optimizedVariableIds) {
        this.program = program;
        this.optimizedVariableIds = optimizedVariableIds;
        PredefinedClasses.getClasses().forEach(clazz -> classByName.put(clazz.clazz.getName(), clazz.clazz));
    }

    protected Variable putVar(String name, Type type, ParserRuleContext ctx) {
        VariableContext variableContext = variableStack.getFirst();
        Variable var = variableContext.variables.get(name);
        if (var != null) {
            if (!type.canBeAssignedTo(var.getType())) {
                throw new ValidationException(ValidationResult.invalid(ctx, "Variable '" + name + "' cannot switch from '" + var.getType() + "' to '" + type + "'"));
            }
            return var;
        } else {
            int id = variableContext.onStack ? variableContext.currentVariableIndex++ : optimizedVariableIds.get(name);
            Variable variable = new Variable(id, type, variableContext.onStack);
            variableContext.variables.put(name, variable);
            return variable;
        }
    }

    protected Variable getVar(String name, ParserRuleContext ctx) {
        Variable variable = variableStack.getFirst().variables.get(name);
        if (variable == null) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Variable " + name + " does not exist"));
        }
        return variable;
    }

    protected boolean variableExists(String name) {
        return variableStack.getFirst().variables.containsKey(name);
    }

    protected void putLabel(String name, ParserRuleContext ctx) {
        if (labelMap.containsKey(name)) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Label '" + name + "' must be unique but is used elsewhere."));
        }
        labelStack.push(name);
        int id = currentLabelIndex++;
        labelMap.put(name, id);
    }

    protected int getLabel(String name, ParserRuleContext ctx) {
        if (!labelStack.contains(name)) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Label '" + name + "' is not owned by any of the loops in this context."));
        }
        return labelMap.get(name);
    }

    protected int popLabel(String name) {
        assert labelStack.getFirst().equals(name);
        labelStack.pop();
        return labelMap.get(name);
    }

    private void enterLoop(QrGameParser.LabelContext labelCtx) {
        loopCounter++;
        if (labelCtx != null) {
            putLabel(labelCtx.NAME().getText(), labelCtx);
        }
    }

    private Integer exitLoop(QrGameParser.LabelContext labelCtx) {
        loopCounter--;
        return labelCtx == null ? null : popLabel(labelCtx.NAME().getText());
    }

    public Stream<ValidationResult> validate() {
        return validators.stream().map(Validator::validate);
    }

    @Override
    public void enterProgram(QrGameParser.ProgramContext ctx) {
        variableStack.push(new VariableContext(false));
        defineStructsAndFunctions(ctx.definition());
    }

    @Override
    public void enterConstDef(QrGameParser.ConstDefContext ctx) {
        definingConstant = true;
    }

    @Override
    public void exitConstDef(QrGameParser.ConstDefContext ctx) {
        definingConstant = false;
        String constName = ctx.CONSTANT().getText();
        if (constants.containsKey(constName)) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Constant with name '" + constName + "' already defined"));
        }
        Expression expression = expressionStack.pop();
        Type type = expression.getType();
        Object value;
        try {
            value = expression.calculate(program);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to calculate constant '" + constName + "'. Did you use a non-constant expression? Error: " + e.getMessage(), e);
        }
        if (type.isBool()) {
            constants.put(constName, new BoolExpression((boolean) value));
        } else if (type.isNumber()) {
            constants.put(constName, new NumberExpression((double) value));
        } else {
            throw new ValidationException(ValidationResult.invalid(ctx, "Constants can only be number or null, was '" + type + "'"));
        }
    }

    @Override
    public final void enterInit(QrGameParser.InitContext ctx) {
        blockStack.push(new ArrayList<>());
    }

    @Override
    public final void exitInit(QrGameParser.InitContext ctx) {
        program.setInitStatement(blockStack.pop().get(0));
    }

    @Override
    public final void enterRun(QrGameParser.RunContext ctx) {
        blockStack.push(new ArrayList<>());
    }

    @Override
    public final void exitRun(QrGameParser.RunContext ctx) {
        program.setCode(blockStack.pop().get(0));
    }

    @Override
    public final void enterBlock(QrGameParser.BlockContext ctx) {
        blockStack.push(new ArrayList<>());
    }

    @Override
    public void enterInput(QrGameParser.InputContext ctx) {
        blockStack.push(new ArrayList<>());
        String buttonIdVarName = ctx.NAME(0).getText();
        String pushedElseReleasedVarName = ctx.NAME(1).getText();
        putVar(buttonIdVarName, NumberType.NUMBER_TYPE, ctx);
        putVar(pushedElseReleasedVarName, BoolType.BOOL_TYPE, ctx);
    }

    @Override
    public void exitInput(QrGameParser.InputContext ctx) {
        Variable buttonIdVarName = getVar(ctx.NAME(0).getText(), ctx);
        Variable pushedElseReleasedVarName = getVar(ctx.NAME(1).getText(), ctx);
        program.setInputCode(new InputCode(buttonIdVarName.getId(), pushedElseReleasedVarName.getId(), blockStack.pop().get(0)));
    }

    @Override
    public final void exitBlock(QrGameParser.BlockContext ctx) {
        ArrayList<Statement> statements = blockStack.pop();
        blockStack.getFirst().add(new BlockStatement(statements));
    }

    @Override
    public void exitStructAssign(QrGameParser.StructAssignContext ctx) {
        Expression valueExpression = expressionStack.pop();
        Expression structExpression = expressionStack.pop();
        String field = ctx.NAME().getText();
        if (!structExpression.getType().isStruct()) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Can only assign field values on structs, was '" + structExpression.getType() + "'"));
        }
        Struct struct = structsById.get(((StructType) structExpression.getType()).getStructId());
        Struct.StructField structField = struct.getFields().get(field);
        if (structField == null) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Field '" + field + "' not present in struct '" + struct.getName() + "'."));
        }
        expressionStack.push(new StructAssignExpression(structExpression, structField.getId(), valueExpression));
        validateCorrectType(valueExpression, structField.getType(), ctx, "Field '" + field + "' is type '" + structField.getType() + "' but got type '" + valueExpression.getType() + "'");
    }

    @Override
    public void exitAssignExpression(QrGameParser.AssignExpressionContext ctx) {
        validateNotDefiningConst(ctx);
        String variableName = ctx.NAME().getSymbol().getText();
        Expression popped = expressionStack.pop();
        Variable variable = putVar(variableName, popped.getType(), ctx);
        expressionStack.push(new AssignExpression(variable.getId(), variable.isOnStack(), popped));
    }

    @Override
    public void exitGetAndIncrement(QrGameParser.GetAndIncrementContext ctx) {
        validateNotDefiningConst(ctx);
        String variable = ctx.NAME().getText();
        Variable var = getVar(variable, ctx);
        expressionStack.push(new GetAndModifyExpression(var.getId(), var.isOnStack(), true));
        if (!var.getType().isNumber()) {
            validators.add(() -> ValidationResult.invalid(ctx, "Variable " + variable + " is not a number, it's " + var.getType()));
        }
    }

    @Override
    public void exitGetAndDecrement(QrGameParser.GetAndDecrementContext ctx) {
        validateNotDefiningConst(ctx);
        String variable = ctx.NAME().getText();
        Variable var = getVar(variable, ctx);
        expressionStack.push(new GetAndModifyExpression(var.getId(), var.isOnStack(), false));
        if (!var.getType().isNumber()) {
            validators.add(() -> ValidationResult.invalid(ctx, "Variable " + variable + " is not a number, it's " + var.getType()));
        }
    }

    @Override
    public void exitIncrementAndGet(QrGameParser.IncrementAndGetContext ctx) {
        addAndGet(ctx, ctx.NAME().getText(), new NumberExpression(1));
    }

    @Override
    public void exitDecrementAndGet(QrGameParser.DecrementAndGetContext ctx) {
        addAndGet(ctx, ctx.NAME().getText(), new NumberExpression(-1));
    }

    @Override
    public void exitModifySubtractException(QrGameParser.ModifySubtractExceptionContext ctx) {
        Expression toAdd = expressionStack.pop();
        addAndGet(ctx, ctx.NAME().getText(), new NegateExpression(toAdd));
        validateCorrectType(toAdd, NumberType.NUMBER_TYPE, ctx, "Expected number type, got '" + toAdd.getType() + "'");
    }

    @Override
    public void exitModifyAddExpression(QrGameParser.ModifyAddExpressionContext ctx) {
        Expression toAdd = expressionStack.pop();
        addAndGet(ctx, ctx.NAME().getText(), toAdd);
        validateCorrectType(toAdd, NumberType.NUMBER_TYPE, ctx, "Expected number type, got '" + toAdd.getType() + "'");
    }

    private void addAndGet(ParserRuleContext ctx, String variableName, Expression toAdd) {
        validateNotDefiningConst(ctx);
        Variable var = getVar(variableName, ctx);
        expressionStack.push(new AssignExpression(var.getId(), var.isOnStack(), new AddExpression(variableExpression(var), toAdd)));
        if (!var.getType().isNumber()) {
            validators.add(() -> ValidationResult.invalid(ctx, "Variable " + variableName + " is not a number, it's " + var.getType()));
        }
    }

    @Override
    public final void enterConditional(QrGameParser.ConditionalContext ctx) {
        blockStack.push(new ArrayList<>());
    }

    @Override
    public final void exitConditional(QrGameParser.ConditionalContext ctx) {
        Expression condition = expressionStack.pop();
        Statement statement = blockStack.pop().get(0);
        blockStack.getFirst().add(new IfStatement(condition, statement));
        validateCorrectType(condition, BoolType.BOOL_TYPE, ctx, "Condition in if statement should be bool, was '" + condition.getType() + "'");
    }

    @Override
    public final void enterConditionalElse(QrGameParser.ConditionalElseContext ctx) {
        blockStack.push(new ArrayList<>());
    }

    @Override
    public final void exitConditionalElse(QrGameParser.ConditionalElseContext ctx) {
        ArrayList<Statement> statementStack = blockStack.pop();
        IfStatement ifStatement = (IfStatement) statementStack.get(0);
        ifStatement.addFalseBody(statementStack.get(1));
        blockStack.getFirst().add(ifStatement);
    }

    @Override
    public final void enterWhileLoop(QrGameParser.WhileLoopContext ctx) {
        blockStack.push(new ArrayList<>());
        enterLoop(ctx.label());
    }

    @Override
    public final void exitWhileLoop(QrGameParser.WhileLoopContext ctx) {
        Statement body = blockStack.pop().get(0);
        Expression condition = expressionStack.pop();
        Integer label = exitLoop(ctx.label());
        blockStack.getFirst().add(new WhileStatement(condition, body, label));
        validateCorrectType(condition, BoolType.BOOL_TYPE, ctx, "Condition in while statement should be bool, was '" + condition.getType() + "'");
    }

    @Override
    public void enterForEachLoop(QrGameParser.ForEachLoopContext ctx) {
        blockStack.push(new ArrayList<>());
        enterLoop(ctx.label());
    }

    @Override
    public void exitForEachLoop(QrGameParser.ForEachLoopContext ctx) {
        Statement body = blockStack.pop().get(0);
        String targetVariable = ctx.forEachCondition().NAME().getText();
        Expression toIterate = expressionStack.pop();
        Variable variable = getVar(targetVariable, ctx.forEachCondition());
        Integer label = exitLoop(ctx.label());
        blockStack.getFirst().add(new ForEachStatement(toIterate, variable.getId(), variable.isOnStack(), body, label));
    }

    @Override
    public void exitForEachCondition(QrGameParser.ForEachConditionContext ctx) {
        String targetVariable = ctx.NAME().getText();
        Expression toIterate = expressionStack.getFirst();
        Type toIterateType = toIterate.getType();
        if (toIterateType.isObject()) {
            ObjectType toIterateObjectType = (ObjectType) toIterateType;
            QgClass<?> classToIterate = toIterateObjectType.getQgClass();
            if (!classToIterate.isIterable()) {
                throw new ValidationException(ValidationResult.invalid(ctx, "Class '" + classToIterate.getName() + "' is not iterable."));
            }
            putVar(targetVariable, classToIterate.iteratorType(toIterateObjectType), ctx);
        } else if (toIterateType.isIterable()) {
            putVar(targetVariable, ((IterableType)toIterateType).getElementType(), ctx);
        } else {
            throw new ValidationException(ValidationResult.invalid(ctx, "Can only iterate on objects and iterable, was '" + toIterateType + "'."));
        }
    }

    @Override
    public void enterForLoop(QrGameParser.ForLoopContext ctx) {
        blockStack.push(new ArrayList<>());
        enterLoop(ctx.label());
    }

    @Override
    public void exitForLoop(QrGameParser.ForLoopContext ctx) {
        List<Statement> endOfLoop = ctx.forLoopPartEnd() == null ?
                Collections.emptyList() :
                getForLoopParts(ctx.forLoopPartEnd().forLoopPart());
        Expression condition = expressionStack.pop();
        List<Statement> init = ctx.forLoopPartInit() == null ?
                Collections.emptyList() :
                getForLoopParts(ctx.forLoopPartInit().forLoopPart());
        Statement body = blockStack.pop().get(0);
        Integer label = exitLoop(ctx.label());
        blockStack.getFirst().add(new BlockStatement(new ArrayList<Statement>(){{
            addAll(init);
            add(new WhileStatement(condition, body, new BlockStatement(new ArrayList<>(endOfLoop)), label));
        }}));
        validateCorrectType(condition, BoolType.BOOL_TYPE, ctx.expression(), "Expression in for-i loop must be of type boolean, was '" + condition.getType() + "'");
    }

    private List<Statement> getForLoopParts(QrGameParser.ForLoopPartContext forLoopPart) {
        if (forLoopPart == null) {
            return Collections.emptyList();
        }
        int expressionCount = countParts(forLoopPart);
        Statement[] expressions = new Statement[expressionCount];
        for (int i = expressionCount - 1; i >= 0; i--) {
            expressions[i] = new ExpressionStatement(expressionStack.pop());
        }
        return Arrays.asList(expressions);
    }

    private int countParts(QrGameParser.ForLoopPartContext forLoopPart) {
        if (forLoopPart == null) {
            return 0;
        }
        return countParts(forLoopPart.forLoopPart()) + 1;
    }

    @Override
    public void exitBreakRule(QrGameParser.BreakRuleContext ctx) {
        Integer label = ctx.NAME() != null ? getLabel(ctx.NAME().getText(), ctx) : null;
        if (label == null && loopCounter == 0) {
            validators.add(() -> ValidationResult.invalid(ctx, "Use of break without being in a loop."));
        }
        blockStack.getFirst().add(new InterruptStatement(true, label));
    }

    @Override
    public void exitContinueRule(QrGameParser.ContinueRuleContext ctx) {
        Integer label = ctx.NAME() != null ? getLabel(ctx.NAME().getText(), ctx) : null;
        if (label == null && loopCounter == 0) {
            validators.add(() -> ValidationResult.invalid(ctx, "Use of continue without being in a loop."));
        }
        blockStack.getFirst().add(new InterruptStatement(false, label));
    }

    @Override
    public void enterWhenStmt(QrGameParser.WhenStmtContext ctx) {
        whenStack.push(WhenBuilder.whenStatementBuilder());
    }

    @Override
    public void enterWhenExpr(QrGameParser.WhenExprContext ctx) {
        whenStack.push(WhenBuilder.whenExpressionBuilder());
    }

    @Override
    public void exitWhenParameter(QrGameParser.WhenParameterContext ctx) {
        whenStack.getFirst().setToCompare(expressionStack.pop());
    }

    @Override
    public void enterWhenStatementClause(QrGameParser.WhenStatementClauseContext ctx) {
        blockStack.push(new ArrayList<>());
    }

    @Override
    public void exitWhenStatementClause(QrGameParser.WhenStatementClauseContext ctx) {
        List<Object> cases = new ArrayList<>();
        boolean hasDefault = getCases(ctx.whenCase(), cases);
        Statement statement = blockStack.pop().get(0);
        for (Object aCase : cases) {
            whenStack.getFirst().putClause(aCase, statement);
        }
        if (hasDefault) {
            whenStack.getFirst().setDefaultCase(statement);
        }
    }

    @Override
    public void exitWhenExpressionClause(QrGameParser.WhenExpressionClauseContext ctx) {
        List<Object> cases = new ArrayList<>();
        boolean hasDefault = getCases(ctx.whenCase(), cases);
        Expression expression = expressionStack.pop();
        for (Object aCase : cases) {
            whenStack.getFirst().putClause(aCase, expression);
        }
        if (hasDefault) {
            whenStack.getFirst().setDefaultCase(expression);
        }
    }

    private boolean getCases(QrGameParser.WhenCaseContext whenCase, List<Object> cases) {
        if (whenCase == null) {
            return false;
        }
        Type whenType = whenStack.getFirst().getParameterType();
        boolean hasDefault = false;
        if (whenCase.atom().NUMBER() != null) {
            if (!whenType.isNumber()) {
                validators.add(() -> ValidationResult.invalid(whenCase, "Type of case was number but type of when parameter was '" + whenType + "'"));
            }
            cases.add(Double.parseDouble(whenCase.atom().NUMBER().getText()));
        } else if (whenCase.atom().BTRUE() != null || whenCase.atom().BFALSE() != null) {
            if (!whenType.isBool()) {
                validators.add(() -> ValidationResult.invalid(whenCase, "Type of case was bool but type of when parameter was '" + whenType + "'"));
            }
            cases.add(whenCase.atom().BTRUE() != null);
        } else if (whenCase.atom().NAME() != null) {
            String word = whenCase.atom().NAME().getText();
            if (word.equals("default")) {
                hasDefault = true;
            } else {
                validators.add(() -> ValidationResult.invalid(whenCase, "Unexpected word '" + word + "'. When cases can only match on values or 'else'."));
            }
        } else {
            throw new ValidationException(ValidationResult.invalid(whenCase, "Unknown atom: " + whenCase.atom()));
        }
        return hasDefault || getCases(whenCase.whenCase(), cases);
    }

    @Override
    public void exitWhenStmt(QrGameParser.WhenStmtContext ctx) {
        blockStack.getFirst().add(whenStack.pop().buildStatement());
    }

    @Override
    public void exitWhenExpr(QrGameParser.WhenExprContext ctx) {
        WhenExpression whenExpression = whenStack.pop().buildExpression();
        if (whenExpression.getDefaultCase() == null) {
            validators.add(() -> ValidationResult.invalid(ctx, "'when' expression must have default case"));
        }
        Type defaultType = whenExpression.getDefaultCase().getType();
        whenExpression.getCases().values()
                .forEach(expression -> validateCorrectType(expression, defaultType, ctx, "Expressions in when clause had different types. Default case is '" + defaultType + "' but found '" + expression.getType() + "' in other cases."));
        expressionStack.push(whenExpression);
    }

    @Override
    public final void exitExpressionStatement(QrGameParser.ExpressionStatementContext ctx) {
        blockStack.getFirst().add(new ExpressionStatement(expressionStack.pop()));
    }

    @Override
    public void exitAdditiveOperation(QrGameParser.AdditiveOperationContext ctx) {
        Expression expression2 = expressionStack.pop();
        Expression expression1 = expressionStack.pop();
        if (ctx.PLUS() != null) {
            expressionStack.push(new AddExpression(expression1, expression2));
        } else if (ctx.MINUS() != null) {
            expressionStack.push(new AddExpression(expression1, new NegateExpression(expression2)));
        } else {
            throw new ValidationException(ValidationResult.invalid(ctx, "Unknown operator"));
        }
        addBiOperatorValidator(ctx, expression2, expression1, NumberType.NUMBER_TYPE);
    }

    @Override
    public void exitAndOperation(QrGameParser.AndOperationContext ctx) {
        Expression rhs = expressionStack.pop();
        Expression lhs = expressionStack.pop();
        expressionStack.push(new AndExpression(lhs, rhs));
        addBiOperatorValidator(ctx, lhs, rhs, BoolType.BOOL_TYPE);
    }

    @Override
    public void exitOrOperation(QrGameParser.OrOperationContext ctx) {
        Expression rhs = expressionStack.pop();
        Expression lhs = expressionStack.pop();
        expressionStack.push(new OrExpression(lhs, rhs));
        addBiOperatorValidator(ctx, lhs, rhs, BoolType.BOOL_TYPE);
    }

    @Override
    public void exitLessComparison(QrGameParser.LessComparisonContext ctx) {
        Expression expression2 = expressionStack.pop();
        Expression expression1 = expressionStack.pop();
        expressionStack.push(new LessThanExpression(expression1, expression2));
        addBiOperatorValidator(ctx, expression1, expression2, NumberType.NUMBER_TYPE);
    }

    @Override
    public void exitGreaterComparison(QrGameParser.GreaterComparisonContext ctx) {
        Expression expression2 = expressionStack.pop();
        Expression expression1 = expressionStack.pop();
        expressionStack.push(new GreaterThanExpression(expression1, expression2));
        addBiOperatorValidator(ctx, expression1, expression2, NumberType.NUMBER_TYPE);
    }

    @Override
    public void exitGreaterEqualsComparison(QrGameParser.GreaterEqualsComparisonContext ctx) {
        Expression expression2 = expressionStack.pop();
        Expression expression1 = expressionStack.pop();
        expressionStack.push(new NotExpression(new LessThanExpression(expression1, expression2)));
        addBiOperatorValidator(ctx, expression1, expression2, NumberType.NUMBER_TYPE);
    }

    @Override
    public void exitLessEqualsComparison(QrGameParser.LessEqualsComparisonContext ctx) {
        Expression expression2 = expressionStack.pop();
        Expression expression1 = expressionStack.pop();
        expressionStack.push(new NotExpression(new GreaterThanExpression(expression1, expression2)));
        addBiOperatorValidator(ctx, expression1, expression2, NumberType.NUMBER_TYPE);
    }

    @Override
    public void exitNegateExpression(QrGameParser.NegateExpressionContext ctx) {
        Expression toNegate = expressionStack.pop();
        expressionStack.push(new NegateExpression(toNegate));
        validateCorrectType(toNegate, NumberType.NUMBER_TYPE, ctx, "Can only negate numbers, was '" + toNegate.getType() + "'");
    }

    @Override
    public void exitNotExpression(QrGameParser.NotExpressionContext ctx) {
        Expression toInvert = expressionStack.pop();
        expressionStack.push(new NotExpression(toInvert));
        validateCorrectType(toInvert, BoolType.BOOL_TYPE, ctx, "Not operator expects boolean, was '" + toInvert.getType() + "'");

    }

    @Override
    public void exitConditionalExpression(QrGameParser.ConditionalExpressionContext ctx) {
        Expression falseCase = expressionStack.pop();
        Expression trueCase = expressionStack.pop();
        Expression condition = expressionStack.pop();
        validateCorrectType(condition, BoolType.BOOL_TYPE, ctx, "Condition should be boolean but was '" + condition.getType() + "'");
        validateCorrectType(trueCase, falseCase.getType(), ctx, "Both branches must be the same type. True case was '" + trueCase.getType() + "' but false case was '" + falseCase.getType() + "'");
        expressionStack.push(new ConditionalExpression(condition, trueCase, falseCase));
    }

    @Override
    public void exitTypeExpression(QrGameParser.TypeExpressionContext ctx) {
        Type type = getType(ctx.type());
        expressionStack.push(new TypeExpression(type));
    }

    @Override
    public void exitAtomicOperation(QrGameParser.AtomicOperationContext ctx) {
        QrGameParser.AtomContext atom = ctx.atom();
        if (atom.NUMBER() != null) {
            double number = Double.parseDouble(atom.NUMBER().getSymbol().getText());
            expressionStack.push(new NumberExpression(number));
        } else if (atom.BFALSE() != null) {
            expressionStack.push(new BoolExpression(false));
        } else if (atom.BTRUE() != null) {
            expressionStack.push(new BoolExpression(true));
        } else if (atom.NAME() != null) {
            validateNotDefiningConst(ctx);
            Variable var = getVar(atom.NAME().getText(), ctx);
            expressionStack.push(variableExpression(var));
        } else if (atom.CONSTANT() != null) {
            String constName = atom.CONSTANT().getText();
            Expression constantExpression = constants.get(constName);
            if (constantExpression == null) {
                throw new ValidationException(ValidationResult.invalid(ctx, "Constant '" + constName + "' not found."));
            }
            expressionStack.push(constantExpression);
        } else {
            throw new RuntimeException("Unhandled atomic case: " + atom.getText());
        }
    }

    private Expression variableExpression(Variable var) {
        if (var.isOnStack()) {
            return new StackVariableExpression(var);
        } else {
            return new VariableExpression(var);
        }
    }

    @Override
    public void exitNullExpression(QrGameParser.NullExpressionContext ctx) {
        expressionStack.push(new NullExpression(getType(ctx.type())));
    }

    @Override
    public void exitMultiplicativeOperation(QrGameParser.MultiplicativeOperationContext ctx) {
        Expression rhs = expressionStack.pop();
        Expression lhs = expressionStack.pop();
        if (ctx.TIMES() != null) {
            expressionStack.push(new MultiplicativeExpression(lhs, rhs));
            addBiOperatorValidator(ctx, lhs, rhs, NumberType.NUMBER_TYPE);
        } else if (ctx.DIV() != null) {
            expressionStack.push(new DivideExpression(lhs, rhs));
            addBiOperatorValidator(ctx, lhs, rhs, NumberType.NUMBER_TYPE);
        } else if (ctx.MOD() != null) {
            expressionStack.push(new ModulusExpression(lhs, rhs));
            addBiOperatorValidator(ctx, lhs, rhs, NumberType.NUMBER_TYPE);
        } else {
            throw new ValidationException(ValidationResult.invalid(ctx, "Unknown operator"));
        }
    }

    @Override
    public void exitEqualsComparison(QrGameParser.EqualsComparisonContext ctx) {
        Expression rhs = expressionStack.pop();
        Expression lhs = expressionStack.pop();
        expressionStack.push(new EqualsExpression(lhs, rhs));
        validateCorrectType(rhs, lhs.getType(), ctx, "LHS is type '" + lhs.getType() + "' but RHS is '" + rhs.getType() + "'");
    }

    @Override
    public void exitNotEqualsComparison(QrGameParser.NotEqualsComparisonContext ctx) {
        Expression rhs = expressionStack.pop();
        Expression lhs = expressionStack.pop();
        validateCorrectType(lhs, rhs.getType(), ctx, "LHS");
        validateCorrectType(rhs, lhs.getType(), ctx, "RHS");
        expressionStack.push(new NotExpression(new EqualsExpression(lhs, rhs)));
        validateCorrectType(rhs, lhs.getType(), ctx, "LHS is type '" + lhs.getType() + "' but rhs is '" + rhs.getType() + "'");
    }

    @Override
    public void exitAReturnStatement(QrGameParser.AReturnStatementContext ctx) {
        if (!activeReturnType.isPresent()) {
            validators.add(() -> ValidationResult.invalid(ctx, "Can only use return in a function"));
            if (ctx.returnStatement().expression() != null) {
                expressionStack.pop();
            }
            return;
        }
        Type returnType = activeReturnType.get();
        if (ctx.returnStatement().expression() != null) {
            Expression returnValue = expressionStack.pop();
            if (!returnValue.getType().canBeAssignedTo(returnType)) {
                validators.add(() -> ValidationResult.invalid(ctx, "Expected value of return to be '" + returnType + "' but it was '" + returnValue.getType() + "'"));
            }
            blockStack.getFirst().add(new ReturnStatement(returnValue));
        } else {
            if (!returnType.isVoid()) {
                validators.add(() -> ValidationResult.invalid(ctx, "Expected return value"));
            }
            blockStack.getFirst().add(new ReturnStatement());
        }
    }

    @Override
    public void exitFunctionRef(QrGameParser.FunctionRefContext ctx) {
        String functionName = ctx.NAME().getText();
        List<Type> argumentTypes = ctx.functionRefParam().stream()
                .map(QrGameParser.FunctionRefParamContext::type)
                .map(this::getType)
                .collect(Collectors.toList());

        UserFunctionDeclaration userFunction = userDeclaredFunctions.get(functionName);
        if (userFunction != null) {
            expressionStack.push(new FunctionReferenceExpression(userFunction.getFunctionId(), true, userFunction.getFunctionType()));
            return;
        }

        PredefinedFunctions.FunctionVariants functionVariants = functionMap.lookupFunction(functionName);
        if (functionVariants == null) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Function '" + functionName + "' not found."));
        }

        PredefinedFunctions.PredefinedFunction theFunction;
        if (functionVariants.hasSeveralVariants()) {
            if (argumentTypes.isEmpty()) {
                throw new ValidationException(ValidationResult.invalid(ctx,
                        "Function '" + functionName + "' has several variants, specify the argument types. " +
                                "For example: ::arrayList(:vararg<number>). The specified function have the following variants: " +
                                System.lineSeparator() + functionVariants
                ));
            } else {
                PredefinedFunctions.PredefinedFunction predefinedFunction = functionVariants.getByTypeParameters(argumentTypes);
                if (predefinedFunction == null) {
                    throw new ValidationException(ValidationResult.invalid(ctx,
                            "No variant matches the specified arguments." + System.lineSeparator() +
                                    "\tArguments specified are: (" + argumentTypes.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")" + System.lineSeparator() +
                                    "\tFunction variants are: " + System.lineSeparator() + functionVariants.toString() + System.lineSeparator()));
                } else {
                    theFunction = predefinedFunction;
                }
            }
        } else {
            theFunction = functionVariants.getSingleVariant();
        }
        expressionStack.push(new FunctionReferenceExpression(theFunction.id, false, theFunction.function.getFunctionType()));
    }

    @Override
    public void exitFunctionCall(QrGameParser.FunctionCallContext ctx) {
        QrGameParser.FunctionContext fctx = ctx.function();
        String functionName = fctx.NAME().getText();

        Expression[] args = collectArgumentExpressions(fctx.argument());
        ArrayList<Expression> arguments = new ArrayList<>(Arrays.asList(args));

        List<Type> genericTip = Collections.emptyList();
        QrGameParser.GenericTipContext genericTipContext = fctx.genericTip();
        if (genericTipContext != null) {
            genericTip = genericTipContext.type().stream()
                    .map(this::getType)
                    .collect(Collectors.toList());
        }

        Variable functionRefVar = variableExists(functionName) ? getVar(functionName, ctx) : null;
        PredefinedFunctions.FunctionVariants functionVariants = functionMap.lookupFunction(functionName);
        UserFunctionDeclaration userFunction = userDeclaredFunctions.get(functionName);
        if (functionRefVar != null) {
            addFunctionRefExpression(fctx, arguments, functionRefVar, functionName, genericTip);
        } else if (userFunction != null) {
            addUserFunction(userFunction, arguments, ctx);
        } else if (functionVariants != null) {
            addBuiltInFunction(ctx, arguments, functionVariants, genericTip);
        } else {
            throw new ValidationException(ValidationResult.invalid(ctx, "Function '" + functionName + "' not found"));
        }
    }

    private void addFunctionRefExpression(QrGameParser.FunctionContext ctx, ArrayList<Expression> arguments, Variable functionRefVar, String varName, List<Type> genericTip) {
        if (!functionRefVar.getType().isFunction()) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Variable '" + varName + "' is not a function reference."));
        }

        FunctionType functionType = (FunctionType) functionRefVar.getType();
        ResultOrInvalidation<Type> returnTypeOrInvalidation = functionType.validate(expressionsToTypes(arguments), genericTip, ctx);
        if (!returnTypeOrInvalidation.hasResult()) {
            throw new ValidationException(returnTypeOrInvalidation.invalidation);
        }

        Expression refExpression = functionRefVar.isOnStack() ? new StackVariableExpression(functionRefVar) : new VariableExpression(functionRefVar);
        expressionStack.push(new FunctionReferenceInvocationExpression(refExpression, arguments, returnTypeOrInvalidation.result));
    }

    private void addBuiltInFunction(QrGameParser.FunctionCallContext ctx, ArrayList<Expression> arguments, PredefinedFunctions.FunctionVariants functionVariants, List<Type> genericTip) {

        ArrayList<Type> argTypes = expressionsToTypes(arguments);
        PredefinedFunctions.PredefinedFunction predefinedFunction = functionVariants.getByTypeParameters(argTypes);

        if (predefinedFunction == null) {
            throw new ValidationException(ValidationResult.invalid(ctx,
                    "No variant matches the specified arguments." + System.lineSeparator() +
                            "\tArguments specified are: (" + argTypes.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")" + System.lineSeparator() +
                            "\tFunction variants are: " + System.lineSeparator() + functionVariants.toString() + System.lineSeparator()));
        }

        Function function = program.getFunction(predefinedFunction.id);

        if (definingConstant && !function.isConstant()) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Function '" + function.getName() + "' is not constant and cannot be used in constants."));
        }

        // Validation can't be delayed as wrong arg count can lead to runtime exceptions
        ResultOrInvalidation<Type> returnTypeOrInvalid = function.getFunctionType().validate(argTypes, genericTip, ctx);
        if (!returnTypeOrInvalid.hasResult()) {
            throw new ValidationException(returnTypeOrInvalid.invalidation);
        }

        if (function.getConstantParameterCount().isPresent() && function.getConstantParameterCount().get() != arguments.size()) {
            int expectedCount = function.getConstantParameterCount().get();
            throw new ValidationException(ValidationResult.invalid(ctx,
                    "Function '" + function.getName() + "' expected '" + expectedCount + "' arguments but got '" + arguments.size() + "'."));
        }
        expressionStack.push(new FunctionExpression(predefinedFunction.id, returnTypeOrInvalid.result, arguments, false));
    }

    private ArrayList<Type> expressionsToTypes(ArrayList<Expression> arguments) {
        return arguments.stream().map(Expression::getType).collect(Collectors.toCollection(ArrayList::new));
    }

    private void addUserFunction(UserFunctionDeclaration userFunction, ArrayList<Expression> arguments, ParserRuleContext ctx) {
        // Validation can't be delayed as wrong arg count can lead to runtime exceptions
        ResultOrInvalidation<Type> returnTypeOrInvalid = userFunction.getFunctionType().validate(expressionsToTypes(arguments), ctx);
        if (!returnTypeOrInvalid.hasResult()) {
            throw new ValidationException(returnTypeOrInvalid.invalidation);
        }
        expressionStack.push(new FunctionExpression(userFunction.getFunctionId(), returnTypeOrInvalid.result, arguments, true));
    }

    @Override
    public void exitMethodCall(QrGameParser.MethodCallContext ctx) {
        QrGameParser.FunctionContext functionContext = ctx.function();
        String methodName = functionContext.NAME().getText();

        Expression[] args = collectArgumentExpressions(functionContext.argument());
        Expression object = expressionStack.pop();

        if (!object.getType().isObject()) {
            throw new ValidationException(
                    ValidationResult.invalid(ctx, "Can only invoke methods on object, was '" + object.getType() + "'"));
        }
        ObjectType objectType = (ObjectType) object.getType();

        // Validation can't be delayed as wrong arg count can lead to runtime exceptions
        ResultOrInvalidation<Type> validationResult = objectType.getQgClass().validate(ctx, objectType, methodName, expressionsToTypes(new ArrayList<>(Arrays.asList(args))));
        if (!validationResult.hasResult()) {
            throw new ValidationException(validationResult.invalidation);
        }

        Integer methodId = objectType.getQgClass().lookupMethodId(methodName);
        expressionStack.push(new MethodCallExpression(object, methodId, Arrays.asList(args), validationResult.result));
    }

    @Override
    public void enterFunctionDefinition(QrGameParser.FunctionDefinitionContext ctx) {
        blockStack.push(new ArrayList<>());
        variableStack.push(new VariableContext(true));
        String name = ctx.NAME().getText();
        FunctionType functionType = userDeclaredFunctions.get(name).getFunctionType();
        activeReturnType = Optional.of(functionType.getReturnType());
        // First parameter will be deepest in the stack which is why we set it to the highest index.
        putVariablesForParameters(ctx.parameters());
    }

    public void putVariablesForParameters(QrGameParser.ParametersContext parametersContext) {
        if (parametersContext == null) return;

        String name = parametersContext.parameter().NAME().getText();
        Type type = getType(parametersContext.parameter().type());
        if (type.isVararg()) {
            type = ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(((VarargType)type).getInner()));
        }
        putVar(name, type, parametersContext.parameter());
        putVariablesForParameters(parametersContext.parameters());
    }

    @Override
    public void exitFunctionDefinition(QrGameParser.FunctionDefinitionContext ctx) {
        Statement body = blockStack.pop().get(0);
        String name = ctx.NAME().getText();
        UserFunctionDeclaration functionDeclaration = userDeclaredFunctions.get(name);

        if (!functionDeclaration.getFunctionType().returnsVoid() && !body.alwaysReturns()) {
            validators.add(() -> ValidationResult.invalid(ctx, "Function '" + name + "' does not deterministically return. Make sure all code branches results in a return."));
        }

        int stackContextSize = variableStack.pop().variables.size();
        program.addUserFunction(new UserFunction(functionDeclaration.getFunctionId(), functionDeclaration.getFunctionType(), stackContextSize, body));
    }

    private Expression[] collectArgumentExpressions(QrGameParser.ArgumentContext argument) {
        int argCount = getArgCount(argument);
        Expression[] args = new Expression[argCount];
        for (int i = argCount - 1; i >= 0; i--) {
            args[i] = expressionStack.pop();
        }
        return args;
    }

    private void defineStructsAndFunctions(List<QrGameParser.DefinitionContext> definition) {
        ArrayList<QrGameParser.StructContext> structs = new ArrayList<>();
        ArrayList<QrGameParser.FunctionDefinitionContext> functions = new ArrayList<>();
        for (QrGameParser.DefinitionContext definitionContext : definition) {
            if (definitionContext.struct() != null) {
                structs.add(definitionContext.struct());
            } else if (definitionContext.functionDefinition() != null) {
                functions.add(definitionContext.functionDefinition());
            } else {
                throw new ValidationException(ValidationResult.invalid(definitionContext, "Unknown definition " + definitionContext.getText()));
            }
        }
        defineStructs(structs);
        declareFunctions(functions);
    }

    private void defineStructs(List<QrGameParser.StructContext> structContexts) {
        structContexts.forEach(this::declareStruct); // Declare empty structs so structs can reference each other
        structContexts.forEach(this::defineStruct); // Fill the declared structs with type data
    }

    private void declareStruct(QrGameParser.StructContext structContext) {
        String structName = structContext.NAME().getText();
        if (structsByName.containsKey(structName)) {
            throw new ValidationException(ValidationResult.invalid(structContext, "A struct with name '" + structName + "' is already defined"));
        }
        if (classByName.containsKey(structName)) {
            throw new ValidationException(ValidationResult.invalid(structContext, "A struct cannot have the same name as a built in class (" + structName + ")"));
        }
        Struct struct = new Struct(structName, currentStructId++);
        structsByName.put(structName, struct);
        structsById.put(struct.getId(), struct);
    }

    private void defineStruct(QrGameParser.StructContext structContext) {
        String structName = structContext.NAME().getText();
        Struct struct = structsByName.get(structName);
        structContext.structField().forEach(fieldCtx -> addField(struct, fieldCtx));
    }

    private void addField(Struct struct, QrGameParser.StructFieldContext fieldCtx) {
        String fieldName = fieldCtx.NAME().getText();
        if (struct.addField(fieldName, getType(fieldCtx.type())) == null) {
            validators.add(() -> ValidationResult.invalid(fieldCtx, "Duplicate field '" + fieldName + "' in struct '" + struct.getName() + "'"));
        }
    }

    private void declareFunctions(List<QrGameParser.FunctionDefinitionContext> functions) {
        functions.forEach(this::declareFunction);
    }

    private void declareFunction(QrGameParser.FunctionDefinitionContext functionDefinitionContext) {
        String name = functionDefinitionContext.NAME().getText();
        if (functionMap.functionWithNameExists(name)) {
            throw new ValidationException(ValidationResult.invalid(functionDefinitionContext, "There is already a built-in function with name '" + name + "'"));
        }
        if (userDeclaredFunctions.containsKey(name)) {
            throw new ValidationException(ValidationResult.invalid(functionDefinitionContext, "Two functions with name '" + name + "'"));
        }
        Type returnType = functionDefinitionContext.type() != null ? getType(functionDefinitionContext.type()) : VoidType.VOID_TYPE;
        ArrayList<Type> parameters = new ArrayList<>();
        getParameters(functionDefinitionContext.parameters(), parameters);
        userDeclaredFunctions.put(name, new UserFunctionDeclaration(name, currentFunctionIndex++, new FunctionType(returnType, parameters)));
    }

    private void getParameters(QrGameParser.ParametersContext parameters, ArrayList<Type> parameterList) {
        if (parameters == null) {
            return;
        }
        Type type = getType(parameters.parameter().type());
        parameterList.add(type);
        getParameters(parameters.parameters(), parameterList);
    }

    private Type getType(QrGameParser.TypeContext typeCtx) {
        return typeCtx.plainType() != null ? getPlainType(typeCtx.plainType()) : getFunctionType(typeCtx.functionType());
    }

    private Type getPlainType(QrGameParser.PlainTypeContext typeCtx) {
        String baseType = typeCtx.NAME().getText();
        switch (baseType) {
            case "number": {
                validateNoTypeArgs(typeCtx, baseType);
                return NumberType.NUMBER_TYPE;
            }
            case "bool": {
                validateNoTypeArgs(typeCtx, baseType);
                return BoolType.BOOL_TYPE;
            }
            case "void": {
                validateNoTypeArgs(typeCtx, baseType);
                return VoidType.VOID_TYPE;
            }
            case "vararg": {
                List<Type> typeArgs = getTypeArguments(typeCtx.genericType());
                if (typeArgs.size() != 1) {
                    throw new ValidationException(ValidationResult.invalid(typeCtx, "Vararg type expects one type parameter but got " + typeArgs.size()));
                }
                Type innerVarargType = typeArgs.get(0);
                if (!VarargType.canContain(innerVarargType)) {
                    throw new ValidationException(ValidationResult.invalid(typeCtx, "Can not construct vararg type of " + innerVarargType));
                }
                return new VarargType(innerVarargType);
            }
            case "iterable": {
                List<Type> typeArgs = getTypeArguments(typeCtx.genericType());
                if (typeArgs.size() != 1) {
                    throw new ValidationException(ValidationResult.invalid(typeCtx, "iterable expects exactly one type parameter"));
                }
                return new IterableType(typeArgs.get(0));
            }
            default: {
                QgClass<?> clazz = classByName.get(baseType);
                if (clazz != null) {
                    List<Type> typeArguments = getTypeArguments(typeCtx.genericType());
                    QgClass.ArgumentCountValidation validationResult = clazz.validateArgumentCount(typeArguments.size());
                    if (!validationResult.isValid()) {
                        throw new ValidationException(ValidationResult.invalid(typeCtx, "Class '" + baseType + "' expects '" + validationResult.getExpectedArgs() + "' type arguments but got '" + typeArguments.size() + "'."));
                    }
                    if (typeArguments.stream().anyMatch(Type::isVararg)) {
                        throw new ValidationException(ValidationResult.invalid(typeCtx, "Classes cannot have vararg type parameters"));
                    }
                    return clazz.getObjectTypeFromTypeArgs(typeArguments);
                }
                Struct struct = structsByName.get(baseType);
                if (struct != null) {
                    validateNoTypeArgs(typeCtx, baseType);
                    return new StructType(struct.getId());
                }
                throw new ValidationException(ValidationResult.invalid(typeCtx, "Undefined type '" + baseType + "'."));
            }
        }
    }

    private Type getFunctionType(QrGameParser.FunctionTypeContext functionType) {
        List<Type> innerTypes = functionType.type().stream()
                .map(this::getType)
                .collect(Collectors.toList());
        return new FunctionType(innerTypes.get(innerTypes.size() - 1), innerTypes.subList(0, innerTypes.size() - 1));
    }

    private void validateNoTypeArgs(QrGameParser.PlainTypeContext typeCtx, final String type) {
        if (typeCtx.genericType() != null) {
            validators.add(() -> ValidationResult.invalid(typeCtx, "Type " + type + " does not have type arguments"));
        }
    }

    private List<Type> getTypeArguments(QrGameParser.GenericTypeContext genericTypeCtx) {
        LinkedList<Type> typeArgs = new LinkedList<>();
        if (genericTypeCtx != null) {
            getTypeArgumentsRecursive(genericTypeCtx.genericTypeArg(), typeArgs);
        }
        return typeArgs;
    }

    private void getTypeArgumentsRecursive(QrGameParser.GenericTypeArgContext typeArgCtx, List<Type> args) {
        if (typeArgCtx == null) {
            return;
        }
        args.add(getType(typeArgCtx.type()));
        getTypeArgumentsRecursive(typeArgCtx.genericTypeArg(), args);
    }

    @Override
    public final void exitStructInstantiation(QrGameParser.StructInstantiationContext ctx) {
        validateNotDefiningConst(ctx);
        String structName = ctx.NAME().getText();
        Struct struct = structsByName.get(structName);

        if (struct == null) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Struct '" + structName + "' not found"));
        }

        Expression[] arguments = collectArgumentExpressions(ctx.argument());
        if (arguments.length != struct.getFields().size()) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Struct constructor for '" + structName + "' expects '" + struct.getFields().size() + "' arguments but got '" + arguments.length + "'"));
        }
        struct.getFields().forEach((name, structField) -> {
            Expression parameter = arguments[structField.getId()];
             validateCorrectType(parameter, structField.getType(), ctx, "Parameter '" + name + "' to '" + structName + "' is type '" + parameter.getType() + "' but expected '" + structField.getType() + "'");
        });
        expressionStack.push(new StructInstantiationExpression(struct.getId(), arguments));
    }

    @Override
    public void exitStructFetch(QrGameParser.StructFetchContext ctx) {
        Expression structExpression = expressionStack.pop();
        String fieldName = ctx.NAME().getText();
        Type type = structExpression.getType();
        if (!type.isStruct()) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Can only fetch fields from structs, was '" + type + "'"));
        }
        Struct struct = structsById.get(((StructType) type).getStructId());
        Struct.StructField structField = struct.getFields().get(fieldName);
        if (structField == null) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Field '" + fieldName + "' does not exist in '" + struct.getName() + "'"));
        }
        expressionStack.push(new StructFetchExpression(structExpression, structField.getType(), structField.getId()));
    }

    private int getArgCount(QrGameParser.ArgumentContext argument) {
        if (argument == null) {
            return 0;
        } else {
            return getArgCount(argument.argument()) + 1;
        }
    }

    protected void validateCorrectType(Expression expression, Type type, ParserRuleContext ctx, String message) {
        validators.add(() -> {
            if (!type.acceptsType(expression.getType())) {
                return ValidationResult.invalid(ctx, message);
            } else {
                return ValidationResult.valid();
            }
        });
    }

    private void addBiOperatorValidator(ParserRuleContext ctx, Expression lhs, Expression rhs, Type type) {
        validateCorrectType(lhs, type, ctx, "LHS should be " + type + ", is '" + lhs.getType() + "'");
        validateCorrectType(rhs, type, ctx, "RHS should be " + type + ", is '" + rhs.getType() + "'");
    }

    private void validateNotDefiningConst(ParserRuleContext ctx) {
        if (definingConstant) {
            throw new ValidationException(ValidationResult.invalid(ctx, "Non-constant expression cannot be used when defining constant."));
        }
    }
}
