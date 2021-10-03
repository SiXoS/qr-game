package se.lindhen.qrgame.program;

import se.lindhen.qrgame.program.drawings.DefaultShapeFactory;
import se.lindhen.qrgame.program.drawings.Shape;
import se.lindhen.qrgame.program.drawings.ShapeFactory;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.UserFunction;
import se.lindhen.qrgame.program.statements.Statement;
import se.lindhen.qrgame.util.ArrayMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class Program {

    private Statement initialisation = null;
    private Statement code = null;
    private InputCode inputCode;

    private ShapeFactory shapeFactory = new DefaultShapeFactory();
    private InputManager inputManager = new InputManager();

    private final ArrayMap<Object> variables = new ArrayMap<>();
    private final PredefinedFunctions functions = new PredefinedFunctions();
    private ArrayList<UserFunction> userFunctions = new ArrayList<>();
    private ArrayList<Object> stack = new ArrayList<>();
    private Object returnValue = null;

    private GameStatus status = GameStatus.RUNNING;
    private final HashMap<Integer, Shape> drawings = new HashMap<>();
    private int score = 0;

    private int secondsSinceStart = 0;
    private double secondsDeltaTime = 0;
    private boolean running = true;
    private boolean canceled = false;
    private Interrupt interrupt = null;
    private boolean trackScore;

    public GameLoop initializeAndPrepareRun() {
        initialisation.run(this);
        return dt -> {
            drawings.clear();
            secondsDeltaTime = dt / 1000.0;
            secondsSinceStart += secondsDeltaTime;
            inputCode.run(this);
            code.run(this);
            drawings.forEach((k, shape) -> shape.update(secondsDeltaTime));
        };
    }

    public Function getFunction(int id) {
        return functions.getFunction(id).function;
    }

    public void setInitStatement(Statement initStatement) {
        initialisation = initStatement;
    }

    public void setCode(Statement code) {
        this.code = code;
    }

    public void setVariable(int id, Object value) {
        variables.put(id, value);
    }

    public Object getVariable(int id) {
        return variables.get(id);
    }

    public void pushToStack(Object value) {
        stack.add(value);
    }

    public void pushAllToStack(Collection<Object> values) {
        stack.addAll(values);
    }

    public Object getFromStack(int index) {
        return stack.get(stack.size() - 1 - index);
    }

    public Object popFromStack() {
        return stack.remove(stack.size() - 1);
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public Statement getInitialisation() {
        return initialisation;
    }

    public Statement getCode() {
        return code;
    }

    public Collection<Shape> getDrawings() {
        return drawings.values();
    }

    public void addDrawing(Shape shape) {
        drawings.put(shape.getId(), shape);
    }

    public double getSecondsSinceStart() {
        return secondsSinceStart;
    }

    public double getSecondsDeltaTime() {
        return secondsDeltaTime;
    }

    public ShapeFactory getShapeFactory() {
        return shapeFactory;
    }

    public void setShapeFactory(ShapeFactory shapeFactory) {
        this.shapeFactory = shapeFactory;
    }

    public void addUserFunction(UserFunction userFunction) {
        if (userFunctions.size() != userFunction.getId()) {
            throw new RuntimeException("User function list size did not match id. Size: " + userFunctions.size() + ", id: " + userFunction.getId());
        }
        userFunctions.add(userFunction);
    }

    public UserFunction getUserFunction(int id) {
        return userFunctions.get(id);
    }

    public ArrayList<UserFunction> getUserFunctions() {
        return userFunctions;
    }

    public void setUserFunctions(ArrayList<UserFunction> userFunctions) {
        this.userFunctions = userFunctions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void setInputCode(InputCode inputCode) {
        this.inputCode = inputCode;
    }

    public InputCode getInputCode() {
        return inputCode;
    }

    public void setReturnValueAndExitFunction(Object value) {
        returnValue = value;
        running = false;
    }

    public Object getReturnValueAndResume() {
        running = true;
        return returnValue;
    }

    public void interruptLoop(Interrupt interrupt) {
        running = false;
        this.interrupt = interrupt;
    }

    public Interrupt catchInterrupt(Integer label) {
        if (interrupt != null) {
            if (!interrupt.hasLabel() || interrupt.getLabel().equals(label)) {
                running = true;
                Interrupt temp = interrupt;
                interrupt = null;
                return temp;
            } else {
                return new Interrupt(null, true);
            }
        }
        return null;
    }

    public boolean isRunning() {
        return running && !canceled;
    }

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setTrackScore(boolean trackScore) {
        this.trackScore = trackScore;
    }

    public boolean getTrackScore() {
        return trackScore;
    }
}
