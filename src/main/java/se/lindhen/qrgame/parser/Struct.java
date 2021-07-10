package se.lindhen.qrgame.parser;

import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.Variable;

import java.util.HashMap;

public class Struct {

    private final int id;
    private int fieldIdCounter = 0;
    private final String name;
    private final HashMap<String, StructField> fields = new HashMap<>();

    public Struct(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public StructField addField(String name, Type type) {
        if (fields.containsKey(name)) {
            return null;
        }
        StructField field = new StructField(fieldIdCounter++, type);
        fields.put(name, field);
        return field;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, StructField> getFields() {
        return fields;
    }

    public static class StructField extends Variable {

        private final Type type;

        public StructField(int id, Type type) {
            super(id, type, false);
            this.type = type;
        }

        public Type getType() {
            return type;
        }
    }
}
