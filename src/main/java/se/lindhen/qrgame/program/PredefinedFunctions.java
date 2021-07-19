package se.lindhen.qrgame.program;

import se.lindhen.qrgame.program.functions.*;
import se.lindhen.qrgame.program.functions.datastructures.*;
import se.lindhen.qrgame.program.functions.math.*;

import java.util.ArrayList;

public class PredefinedFunctions {

    private static final ArrayList<PredefinedFunction> predefinedFunctions = new ArrayList<PredefinedFunction>() {{
        add(new PredefinedFunction(new DrawFunction()));
        add(new PredefinedFunction(new RandFunction()));
        add(new PredefinedFunction(new TimeFunction()));
        add(new PredefinedFunction(new GetScoreFunction()));
        add(new PredefinedFunction(new SetScoreFunction()));
        add(new PredefinedFunction(new ModifyScoreFunction()));
        add(new PredefinedFunction(new WonFunction()));
        add(new PredefinedFunction(new LostFunction()));
        add(new PredefinedFunction(new EllipseFunction()));
        add(new PredefinedFunction(new RectangleFunction()));
        add(new PredefinedFunction(new CompositeShapeFunction()));
        add(new PredefinedFunction(new IndexedHashSetFunction()));
        add(new PredefinedFunction(new TreeSetFunction()));
        add(new PredefinedFunction(new LinkedListFunction()));
        add(new PredefinedFunction(new ArrayListFunction()));
        add(new PredefinedFunction(new HashSetFunction()));
        add(new PredefinedFunction(new MapEntryFunction()));
        add(new PredefinedFunction(new TreeMapFunction()));
        add(new PredefinedFunction(new HashMapFunction()));
        add(new PredefinedFunction(new AbsFunction()));
        add(new PredefinedFunction(new ACosFunction()));
        add(new PredefinedFunction(new ASinFunction()));
        add(new PredefinedFunction(new ATanFunction()));
        add(new PredefinedFunction(new CeilFunction()));
        add(new PredefinedFunction(new CosFunction()));
        add(new PredefinedFunction(new DegToRadFunction()));
        add(new PredefinedFunction(new EFunction()));
        add(new PredefinedFunction(new ExpFunction()));
        add(new PredefinedFunction(new FloorFunction()));
        add(new PredefinedFunction(new IntDivFunction()));
        add(new PredefinedFunction(new IntModFunction()));
        add(new PredefinedFunction(new Log10Function()));
        add(new PredefinedFunction(new LogFunction()));
        add(new PredefinedFunction(new LognFunction()));
        add(new PredefinedFunction(new MaxFunction()));
        add(new PredefinedFunction(new MinFunction()));
        add(new PredefinedFunction(new PiFunction()));
        add(new PredefinedFunction(new PowFunction()));
        add(new PredefinedFunction(new RadToDegFunction()));
        add(new PredefinedFunction(new RoundFunction()));
        add(new PredefinedFunction(new SinFunction()));
        add(new PredefinedFunction(new SqrtFunction()));
        add(new PredefinedFunction(new TanFunction()));
        add(new PredefinedFunction(new TriangleFunction()));
    }};

    private PredefinedFunctions() {
    }

    public static Iterable<PredefinedFunction> getFunctions() {
        return predefinedFunctions;
    }

    public static class PredefinedFunction {
        private static int idCounter = 0;
        public final Function function;
        public final int id = idCounter++;

        private PredefinedFunction(Function function) {
            this.function = function;
        }
    }

}
