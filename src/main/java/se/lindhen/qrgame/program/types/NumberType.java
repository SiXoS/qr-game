package se.lindhen.qrgame.program.types;

public class NumberType extends Type {

    public static final NumberType NUMBER_TYPE = new NumberType();

    private NumberType() {
        super(BaseType.NUMBER);
    }

    @Override
    public boolean isComparable() {
        return true;
    }

}
