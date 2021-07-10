package se.lindhen.qrgame.program;

import se.lindhen.qrgame.program.objects.*;

import java.util.ArrayList;

public class PredefinedClasses {

    private static final ArrayList<PredefinedClass> predefinedClasses = new ArrayList<PredefinedClass>() {{
        add(new PredefinedClass(ShapeClass.getQgClass()));
        add(new PredefinedClass(ListClass.getQgClass()));
        add(new PredefinedClass(TreeSetClass.getQgClass()));
        add(new PredefinedClass(HashSetClass.getQgClass()));
        add(new PredefinedClass(IndexedHashSetClass.getQgClass()));
        add(new PredefinedClass(HashMapClass.getQgClass()));
        add(new PredefinedClass(MapEntryClass.getQgClass()));
        add(new PredefinedClass(TreeMapClass.getQgClass()));
    }};

    private PredefinedClasses(){}

    public static Iterable<PredefinedClass> getClasses() {
        return predefinedClasses;
    }

    public static class PredefinedClass {
        private static int idCounter = 0;
        public final QgClass<?> clazz;
        public final int id = idCounter++;

        private PredefinedClass(QgClass<?> clazz) {
            this.clazz = clazz;
        }
    }

}
