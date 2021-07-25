package se.lindhen.qrgame.program.types;

public class BoolType extends Type {

    public final static BoolType BOOL_TYPE = new BoolType();

    private BoolType() {
        super(BaseType.BOOL);
    }

    @Override
    public boolean isComparable() {
        return true;
    }

    @Override
    public String toString() {
        return "bool";
    }
}
