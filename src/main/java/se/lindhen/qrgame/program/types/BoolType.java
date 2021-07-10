package se.lindhen.qrgame.program.types;

import java.util.Objects;

public class BoolType extends Type {

    public final static BoolType BOOL_TYPE = new BoolType();

    private BoolType() {
        super(BaseType.BOOL);
    }

    @Override
    public boolean isComparable() {
        return true;
    }
}
