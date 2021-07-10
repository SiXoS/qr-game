package se.lindhen.qrgame.program;

import java.util.ArrayList;

public class StructValue {

    private final ArrayList<Object> fields;

    public StructValue(ArrayList<Object> fields) {
        this.fields = fields;
    }

    public Object getField(int fieldId) {
        return fields.get(fieldId);
    }

    public Object setField(int fieldId, Object newValue) {
        return fields.set(fieldId, newValue);
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if(!(obj instanceof StructValue)) {
            return false;
        }
        return fields.equals(((StructValue) obj).fields);
    }
}
