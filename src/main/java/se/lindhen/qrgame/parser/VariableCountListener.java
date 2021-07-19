package se.lindhen.qrgame.parser;

import se.lindhen.qrgame.QrGameBaseListener;
import se.lindhen.qrgame.QrGameParser;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VariableCountListener extends QrGameBaseListener {

    private final HashMap<String, Integer> varCounts = new HashMap<>();
    private HashSet<String> stackVariables = new HashSet<>();

    @Override
    public void exitFunctionDefinition(QrGameParser.FunctionDefinitionContext ctx) {
        stackVariables.clear();
    }

    @Override
    public void enterParameter(QrGameParser.ParameterContext ctx) {
        stackVariables.add(ctx.NAME().getText());
    }

    @Override
    public void enterAtom(QrGameParser.AtomContext ctx) {
        if (ctx.NAME() != null) {
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
