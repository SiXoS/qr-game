package se.lindhen.qrgame.program;

public class Interrupt {

    private final Integer label;
    private final boolean breakElseContinue;

    public Interrupt(Integer label, boolean breakElseContinue) {
        this.label = label;
        this.breakElseContinue = breakElseContinue;
    }

    public Integer getLabel() {
        return label;
    }

    public boolean hasLabel() {
        return label != null;
    }

    public boolean isBreakElseContinue() {
        return breakElseContinue;
    }
}
