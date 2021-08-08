# QR Game

![Build status](https://github.com/SiXoS/qr-game/actions/workflows/build.yml/badge.svg)

Compiler and runtime for the QG language. QG is designed for creating games that can be compiled to a QR code.
The language focuses on small byte code footprint, ease of use and basic type safety.

## Usage

Compile a program using:
```
mvn install
java -jar target/qr-game-compiler-1.0-SNAPSHOT-jar-with-dependencies.jar compile code.qg output_qr.png
```

## Small example
Example UI and code for a small "game". It with a square in the center. 
When you hit a d-pad button the square will move.

### Game UI
This is an image of the game UI. The buttons are static for all games but the behavior is controlled through code.

![Image of simple game UI](readme_resources/minimal_game_ui.jpg)

### Code

```
init {
  position = -1
}

run {
  coords = when(position) {
    0 -> new pos(490, 390)
    1 -> new pos(590, 490)
    2 -> new pos(490, 590)
    3 -> new pos(390, 490)
    default -> new pos(490, 490)
  }
  draw(createRectangle(coords.x, coords.y, 20, 20))
}

input(button, pressedElseReleased) {
  if (pressedElseReleased) {
    position = button % 4
  }
}

struct pos {
  x: number
  y: number
}
```

### QR code
The compiled bytecode is 64 bytes and is stored in the below QR code. The maximum number
of bytes in a QR is 2952.

![QR for the minimal game](readme_resources/minimal_game_qr.png)
