package se.lindhen.qrgame.program;

import java.util.ArrayList;

public class InputManager {

    private final ArrayList<ButtonEvent> buttonEvents = new ArrayList<>();

    public void triggerButton(Input button, boolean pressedElseReleased) {
        buttonEvents.add(new ButtonEvent(button, pressedElseReleased));
    }

    ArrayList<ButtonEvent> getButtonEvents() {
        return buttonEvents;
    }

    void clear() {
        buttonEvents.clear();
    }

    public enum Input {
        LEFT_TOP(0),
        LEFT_RIGHT(1),
        LEFT_BOTTOM(2),
        LEFT_LEFT(3),
        RIGHT_TOP(4),
        RIGHT_RIGHT(5),
        RIGHT_BOTTOM(6),
        RIGHT_LEFT(7);

        private final int id;

        Input(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public int getId() {
            return id;
        }
    }

    static class ButtonEvent {
        final Input button;
        final boolean pressedElseReleased;

        public ButtonEvent(Input button, boolean pressedElseReleased) {
            this.button = button;
            this.pressedElseReleased = pressedElseReleased;
        }
    }

}
