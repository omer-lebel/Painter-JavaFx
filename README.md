# Painter Application

A simple drawing application built with JavaFX.

## Features

- Pencil, eraser, shape drawing (line, circle, rectangle, triangle)
- Undo and clear options
- Change fill, stroke, color, opacity, and brush size

![Painter App Demo](https://github.com/omer-lebel/PainterJavaFx/blob/master/demo.gif?raw=true)

## Running the Application
Make sure you have [JDK 11](https://jdk.java.net/) or higher installed and [JavaFX SDK](https://openjfx.io/) set up properly.
> Replace `"/path/to/javafx-sdk/lib"` with the path to your JavaFX SDK lib directory.

```bash
# Compile
javac --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml *.java

# Run
java --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml PainterApp
```

## License

[MIT](https://choosealicense.com/licenses/mit/)
