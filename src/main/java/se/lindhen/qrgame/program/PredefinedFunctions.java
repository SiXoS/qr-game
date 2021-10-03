package se.lindhen.qrgame.program;

import javafx.util.Pair;
import se.lindhen.qrgame.program.functions.*;
import se.lindhen.qrgame.program.functions.datastructures.*;
import se.lindhen.qrgame.program.functions.math.*;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.util.ArrayMap;

import java.util.*;

public class PredefinedFunctions {

    private final ArrayMap<PredefinedFunction> functionsById;
    private final HashMap<String, FunctionVariants> functionsByName;

    public PredefinedFunctions() {
        List<PredefinedFunction> allFunctions = getPredefinedFunctions();
        functionsById = new ArrayMap<>();
        allFunctions.forEach(func -> functionsById.put(func.id, func));
        functionsByName = new HashMap<>();
        allFunctions.forEach(func -> {
            if (!func.legacy) {
                functionsByName.computeIfAbsent(func.function.getName(), key -> new FunctionVariants()).addVariant(func);
            }
        });
    }

    private static ArrayList<PredefinedFunction> getPredefinedFunctions() {
        ArrayList<PredefinedFunction> predefinedFunctions = new ArrayList<PredefinedFunction>() {{
            // drawings
            add(new PredefinedFunction(new DrawFunction(), 0));
            add(new PredefinedFunction(new EllipseFunction(), 8));
            add(new PredefinedFunction(new RectangleFunction(), 9));
            add(new PredefinedFunction(new CompositeShapeFunction(), 10));
            add(new PredefinedFunction(new TriangleFunction(), 43));

            // misc
            add(new PredefinedFunction(new RandFunction(), 1));
            add(new PredefinedFunction(new TimeFunction(), 2));
            add(new PredefinedFunction(new AssertFunction(), 44));
            add(new PredefinedFunction(new TimeDiffFunction(), 45));

            // game state
            add(new PredefinedFunction(new GetScoreFunction(), 3));
            add(new PredefinedFunction(new SetScoreFunction(), 4));
            add(new PredefinedFunction(new ModifyScoreFunction(), 5));
            add(new PredefinedFunction(new WonFunction(), 6));
            add(new PredefinedFunction(new LostFunction(), 7));

            // data structures
            add(new PredefinedFunction(new MapEntryFunction(), 16));
            add(new PredefinedFunction(new ArrayListFromTypeFunction(), 46));
            add(new PredefinedFunction(new ArrayListFromTypeSizeFunction(), 47));
            add(new PredefinedFunction(new ArrayListFromVarargFunction(), 48));
            add(new PredefinedFunction(new HashMapFromTypeFunction(), 49));
            add(new PredefinedFunction(new HashMapFromVarargFunction(), 50));
            add(new PredefinedFunction(new HashSetFromTypeFunction(), 51));
            add(new PredefinedFunction(new HashSetFromVarargFunction(), 52));
            add(new PredefinedFunction(new IndexedHashSetFromTypeFunction(), 53));
            add(new PredefinedFunction(new IndexedHashSetFromVarargFunction(), 54));
            add(new PredefinedFunction(new LinkedListFromTypeFunction(), 55));
            add(new PredefinedFunction(new LinkedListFromVarargFunction(), 56));
            add(new PredefinedFunction(new MapEntryFunction(), 57));
            add(new PredefinedFunction(new TreeMapFromTypeFunction(), 58));
            add(new PredefinedFunction(new TreeMapFromVarargFunction(), 59));
            add(new PredefinedFunction(new TreeSetFromTypeFunction(), 60));
            add(new PredefinedFunction(new TreeSetFromVarargFunction(), 61));

            // math
            add(new PredefinedFunction(new AbsFunction(), 19));
            add(new PredefinedFunction(new ACosFunction(), 20));
            add(new PredefinedFunction(new ASinFunction(), 21));
            add(new PredefinedFunction(new ATanFunction(), 22));
            add(new PredefinedFunction(new CeilFunction(), 23));
            add(new PredefinedFunction(new CosFunction(), 24));
            add(new PredefinedFunction(new DegToRadFunction(), 25));
            add(new PredefinedFunction(new EFunction(), 26));
            add(new PredefinedFunction(new ExpFunction(), 27));
            add(new PredefinedFunction(new FloorFunction(), 28));
            add(new PredefinedFunction(new IntDivFunction(), 29));
            add(new PredefinedFunction(new IntModFunction(), 30));
            add(new PredefinedFunction(new Log10Function(), 31));
            add(new PredefinedFunction(new LogFunction(), 32));
            add(new PredefinedFunction(new LognFunction(), 33));
            add(new PredefinedFunction(new MaxFunction(), 34));
            add(new PredefinedFunction(new MinFunction(), 35));
            add(new PredefinedFunction(new PiFunction(), 36));
            add(new PredefinedFunction(new PowFunction(), 37));
            add(new PredefinedFunction(new RadToDegFunction(), 38));
            add(new PredefinedFunction(new RoundFunction(), 39));
            add(new PredefinedFunction(new SinFunction(), 40));
            add(new PredefinedFunction(new SqrtFunction(), 41));
            add(new PredefinedFunction(new TanFunction(), 42));

            // legacy functions (supported in bytecode but not in the language)
            add(new PredefinedFunction(new TreeMapLegacyFunction(), 17, true));
            add(new PredefinedFunction(new HashMapLegacyFunction(), 18, true));
            add(new PredefinedFunction(new IndexedHashSetLegacyFunction(), 11, true));
            add(new PredefinedFunction(new TreeSetLegacyFunction(), 12, true));
            add(new PredefinedFunction(new LinkedListLegacyFunction(), 13, true));
            add(new PredefinedFunction(new ArrayListLegacyFunction(), 14, true));
            add(new PredefinedFunction(new HashSetLegacyFunction(), 15, true));

        }};
        validateNoHolesAndNoDuplicates(predefinedFunctions);
        return predefinedFunctions;
    }

    private static void validateNoHolesAndNoDuplicates(ArrayList<PredefinedFunction> predefinedFunctions) {
        HashMap<Integer, PredefinedFunction> functions = new HashMap<>();
        int maxId = predefinedFunctions.size() - 1;
        for (PredefinedFunction predefinedFunction : predefinedFunctions) {
            if (predefinedFunction.id > maxId) {
                throw new IllegalArgumentException("Function '" + predefinedFunction.function.getName() + "' with id '" + predefinedFunction.id + "' has an id which is too large. Ids must be consecutive.");
            }
            PredefinedFunction current = functions.get(predefinedFunction.id);
            if (current != null) {
                throw new IllegalArgumentException("Functions '" + current.function.getName() + "' and '" + predefinedFunction.function.getName() + "' has the same id '" + predefinedFunction.id + "'");
            }
            functions.put(predefinedFunction.id, predefinedFunction);
        }
    }

    public PredefinedFunction getFunction(int id) {
        return functionsById.get(id);
    }

    public PredefinedFunction lookupFunction(String name, List<Type> parameterTypes) {
        FunctionVariants functionVariants = functionsByName.get(name);
        if (functionVariants == null) return null;
        PredefinedFunction funcVariant = functionVariants.getByTypeParameters(parameterTypes);
        if (funcVariant == null) return null;
        if (funcVariant.legacy) return null;
        return funcVariant;
    }

    public boolean functionWithNameExists(String name) {
        return functionsByName.containsKey(name);
    }

    private static class FunctionVariants {
        private final ArrayList<ParameterizedFunction> variants = new ArrayList<>();

        private boolean hasVariants() {
            return variants.size() > 1;
        }

        private void addVariant(PredefinedFunction predefinedFunction) {
            List<Type> functionParameters = predefinedFunction.function.getFunctionDeclaration().getFunctionParameters();
            variants.add(new ParameterizedFunction(functionParameters, predefinedFunction));
        }

        private PredefinedFunction getByTypeParameters(List<Type> typeParameters) {
            for (ParameterizedFunction parameterizedFunction : variants) {
                if (parameterizedFunction.accepts(typeParameters)) {
                    return parameterizedFunction.getPredefinedFunction();
                }
            }
            return null;
        }

    }
    public static class PredefinedFunction {
        public final int id;
        public final boolean legacy;

        public final Function function;

        private PredefinedFunction(Function function, int id) {
            this(function, id, false);
        }
        /**
         *
         * @param function
         * @param id
         * @param legacy Legacy functions cannot be used when compiling a software but can be looked up by id
         *               (when decompiling). Legacy functions are only kept for bytecode compatibility.
         */
        private PredefinedFunction(Function function, int id, boolean legacy) {
            this.function = function;
            this.id = id;
            this.legacy = legacy;
        }

    }

    private static class ParameterizedFunction {

        private final ArrayList<Type> parameters;
        private final PredefinedFunction predefinedFunction;

        public ParameterizedFunction(List<Type> parameters, PredefinedFunction predefinedFunction) {
            this.parameters = new ArrayList<>(parameters);
            this.predefinedFunction = predefinedFunction;
            assert parameters.stream().limit(Math.max(parameters.size() - 1, 0)).noneMatch(Type::isVararg): "Only the last type parameter can be vararg";
        }

        public PredefinedFunction getPredefinedFunction() {
            return predefinedFunction;
        }

        public boolean accepts(List<Type> params) {
            if (parameters.isEmpty()) {
                return params.isEmpty();
            }
            if (params.size() < parameters.size()) return false;
            for (int i = 0; i < parameters.size() - 1; i++) {
                if(!parameters.get(i).acceptsType(params.get(i))) {
                    return false;
                }
            }
            int lastParameterIndex = parameters.size() - 1;
            Type lastParameter = parameters.get(lastParameterIndex);
            if (lastParameter.isVararg()) {
                for (int i = lastParameterIndex; i < params.size(); i++) {
                    if (!lastParameter.acceptsType(params.get(i))) return false;
                }
            } else {
                if (params.size() != parameters.size()) return false;
                if (!lastParameter.acceptsType(params.get(lastParameterIndex))) return false;
            }
            return true;
        }

    }

}
