package se.lindhen.qrgame.parser;

import se.lindhen.qrgame.QrGameBaseListener;
import se.lindhen.qrgame.QrGameParser;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VariableCountListener extends QrGameBaseListener {

    private final HashMap<String, Integer> varCounts = new HashMap<>();
    private final HashSet<String> stackVariables = new HashSet<>();
    private int functionDepth = 0;

    @Override
    public void enterFunctionDefinition(QrGameParser.FunctionDefinitionContext ctx) {
        functionDepth++;
    }

    @Override
    public void exitFunctionDefinition(QrGameParser.FunctionDefinitionContext ctx) {
        functionDepth--;
    }

    @Override
    public void enterParameter(QrGameParser.ParameterContext ctx) {
        stackVariables.add(ctx.NAME().getText());
    }

    @Override
    public void enterInput(QrGameParser.InputContext ctx) {
        trackVariable(ctx.NAME(0).getText());
        trackVariable(ctx.NAME(1).getText());
    }

    @Override
    public void enterAtom(QrGameParser.AtomContext ctx) {
        if (functionDepth == 0 && ctx.NAME() != null) {
            trackVariable(ctx.NAME().getText());
        }
    }

    @Override
    public void enterAssignExpression(QrGameParser.AssignExpressionContext ctx) {
        trackVariable(ctx.NAME().getText());
    }

    private void trackVariable(String varName) {
        if (stackVariables.contains(varName)) return;

        varCounts.compute(varName, (key, count) -> count == null ? 0 : count + 1);
    }

    public HashMap<String, Integer> getOptimizedVariableIds() {
        AtomicInteger varId = new AtomicInteger(0);
        HashMap<String, Integer> varIdMap = new HashMap<>();
        varCounts.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEach(entry -> varIdMap.put(entry.getKey(), varId.getAndIncrement()));
        return varIdMap;
    }
}
