package se.lindhen.qrgame.program.types;

public class VoidType extends Type {

    public static final VoidType VOID_TYPE = new VoidType();

    private VoidType() {
        super(BaseType.VOID);
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public String toString() {
        return "void";
    }
}
