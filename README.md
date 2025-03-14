# Painter Application

A simple drawing application built with JavaFX, which allows users to draw freehand, shapes, and use an eraser tool. The application provides a variety of brush sizes, colors, and opacity settings to enhance the drawing experience.

## Features

- Pencil, eraser, shape drawing (line, circle, rectangle, triangle)
- Undo and clear options
- Change fill, stroke, color, opacity, and brush size

![Painter App Demo](https://github.com/omer-lebel/Painter-JavaFx/blob/master/screenshot.gif?raw=true)

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