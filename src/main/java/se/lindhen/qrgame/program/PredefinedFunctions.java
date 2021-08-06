package se.lindhen.qrgame.program;

import se.lindhen.qrgame.program.functions.*;
import se.lindhen.qrgame.program.functions.datastructures.*;
import se.lindhen.qrgame.program.functions.math.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PredefinedFunctions {

    private static final ArrayList<PredefinedFunction> predefinedFunctions = getPredefinedFunctions();

    private PredefinedFunctions() {
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
            add(new PredefinedFunction(new IndexedHashSetFunction(), 11));
            add(new PredefinedFunction(new TreeSetFunction(), 12));
            add(new PredefinedFunction(new LinkedListFunction(), 13));
            add(new PredefinedFunction(new ArrayListFunction(), 14));
            add(new PredefinedFunction(new HashSetFunction(), 15));
            add(new PredefinedFunction(new MapEntryFunction(), 16));
            add(new PredefinedFunction(new TreeMapFunction(), 17));
            add(new PredefinedFunction(new HashMapFunction(), 18));

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

    public static Iterable<PredefinedFunction> getFunctions() {
        return predefinedFunctions;
    }

    public static class PredefinedFunction {
        public final int id;
        public final Function function;

        private PredefinedFunction(Function function, int id) {
            this.function = function;
            this.id = id;
        }
    }

}
