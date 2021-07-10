package se.lindhen.qrgame.program;

import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;
import java.util.List;

public class StructDefinition {

    private final int structId;
    private final ArrayList<Type> fields = new ArrayList<>();

    public StructDefinition(int structId) {
        this.structId = structId;
    }

    public int getStructId() {
        return structId;
    }

    public void setFields(List<Type> fields) {
        this.fields.clear();
        this.fields.addAll(fields);
    }

    public List<Type> getFields() {
        return fields;
    }
}
