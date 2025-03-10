import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class PainterController {

    // shapes icons and fill checkbox
    @FXML private Line lineIcon;
    @FXML private Circle circleIcon;
    @FXML private Rectangle rectangleIcon;
    @FXML private Polygon triangleIcon;

    // fill toggle group
    @FXML private ToggleGroup fillToggleGroup;
    @FXML private RadioButton emptyRadioBtn;
    @FXML private RadioButton fullRadioBtn;

    //color picker
    @FXML private ColorPicker colorPicker;

    // size slider
    @FXML private Slider sizeSlider;
    @FXML private TextField sizeTextField;

    // opacity slider
    @FXML private Slider opacitySlider;
    @FXML private TextField opacityTextField;

    // drawing area pane
    @FXML private Pane drawingAreaPane;


    // -------------------- manage components --------------------

    /**
     * Current state of the painter
     */
    private enum State{
        PENCIL,
        ERASER,
        LINE,
        CIRCLE,
        RECTANGLE,
        TRIANGLE;
    }

    private Color brashColor;
    private Color fillColor;
    private double size;
    private State currState;
    private double xStart, yStart;
    private boolean whileDragging;

    /**
     * Initializes the PainterController.
     */
    public void initialize(){
        brashColor = Color.BLACK;
        fillColor = Color.TRANSPARENT;
        size = sizeSlider.getValue();
        currState = State.LINE;
        whileDragging = false;

        initRadioBtns();
        initSizeSlider();
        initOpacitySlider();

    }

    /**
     * Initializes the empty anf fill radio buttons.
     */
    private void initRadioBtns(){
        fullRadioBtn.setUserData(brashColor);
        emptyRadioBtn.setUserData(Color.TRANSPARENT);
    }

    /**
     * Initializes the size slider.
     */
    private void initSizeSlider(){
        sizeTextField.textProperty().bind(sizeSlider.valueProperty().asString("%.0f"));
        sizeSlider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
                        size = newVal.doubleValue();
                    }
                }
        );
    }

    /**
     * Initializes the opacity slider.
     */
    private void initOpacitySlider(){
        opacityTextField.textProperty().bind(opacitySlider.valueProperty().asString("%.2f"));
        opacitySlider.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
                        brashColor = changeColorOpacity(brashColor, newVal.doubleValue());
                        fillColor = changeColorOpacity(fillColor, newVal.doubleValue());
                    }
                }
        );
    }

    /**
     * Changes the curr state of the painter according to the pressed button
     */
    @FXML void pencilBtnPressed(ActionEvent event)      { currState = State.PENCIL; }
    @FXML void eraserBtnPressed(ActionEvent event)      { currState = State.ERASER; }
    @FXML void circleBtnPressed(ActionEvent event)      { currState = State.CIRCLE; }
    @FXML void lineBtnPressed(ActionEvent event)        { currState = State.LINE; }
    @FXML void rectangleBtnPressed(ActionEvent event)   { currState = State.RECTANGLE; }
    @FXML void triangleBtnPressed(ActionEvent event)    { currState = State.TRIANGLE; }

    /**
     * Handles the color picked event.
     */
    @FXML
    void onColorPicked(ActionEvent event) {
        brashColor = colorPicker.getValue();
        fillColor = fillColor.equals(Color.TRANSPARENT) ? fillColor : brashColor;

        setIconsStroke(lineIcon, circleIcon, rectangleIcon, triangleIcon);
        setIconsFill(circleIcon, rectangleIcon, triangleIcon);

    }

    /**
     * Sets the fill color of the given shapes to the fillColor
     */
    private void setIconsFill(Shape... shapes){
        for (Shape shape : shapes){
            shape.setFill(fillColor);
        }
    }

    /**
     * Sets the stroke color of the given shapes to the fillColor
     */
    private void setIconsStroke(Shape... shapes){
        for (Shape shape : shapes){
            shape.setStroke(brashColor);
        }
    }

    /**
     * Handles the fill radio button selected event
     * by changing the fill color according to the pressed radio button
     */
    @FXML
    void fillRadioBtnSelected(ActionEvent event) {
        fillColor = fillColor.equals(brashColor) ? Color.TRANSPARENT : brashColor;
        setIconsFill(circleIcon, rectangleIcon, triangleIcon);
    }

    /**
     * Handles the undo button press event,
     * by removing the last shape that was painted
     */
    @FXML
    void undoBtnPressed(ActionEvent event) {
        removeLast();
    }

    /**
     * Removes the last shape that was painted
     */
    private void removeLast(){
        int count = drawingAreaPane.getChildren().size();
        if (count > 0){
            drawingAreaPane.getChildren().remove(count - 1);
        }
    }

    /**
     * Handles the clear button press event,
     * by clearing all the drawing area
     */
    @FXML
    void clearBtnPressed(ActionEvent event) {
        drawingAreaPane.getChildren().clear();
    }


    // -------------------- mouse events --------------------
    /**
     * Handles the mouse pressed event,
     * by painting on the board (with pencil or eraser)
     */
    @FXML
    void onMousePressed(MouseEvent event) {
        xStart = event.getX();
        yStart = event.getY();

        if (currState == State.PENCIL || currState == State.ERASER){
            Color color = (currState == State.PENCIL) ? brashColor : Color.WHITE;
            paintFreeStyle(xStart, yStart, color);
        }
    }

    /**
     * Handles the mouse dragged event,
     * by painting on the board if it's pencil/eraser,
     * or by showing preview shape.
     */
    @FXML
    void onMouseDragged(MouseEvent event) {
        double x =event.getX();
        double y = event.getY();

        if (currState == State.PENCIL || currState == State.ERASER){
            handlePencilOrEraser(x, y);
        }
        else {
            handleShapePreview(x, y);
        }
    }

    /**
     * Handles pencil or eraser action during mouse drag.
     *
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    private void handlePencilOrEraser(double x, double y) {
        Color color = (currState == State.PENCIL) ? brashColor : Color.WHITE;
        paintFreeStyle(x, y, color);
    }

    /**
     * Handles shape preview during mouse drag.
     *
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    private void handleShapePreview(double x, double y) {
        adjustColorOpacityForDragging();
        cleanDraggedShape();
        paintShape(x, y);
        restoreOriginalColorOpacity();
        whileDragging = true; // Indicates that the shape will be removed
    }

    /**
     * Adjusts color opacity for dragging shapes.
     */
    private void adjustColorOpacityForDragging() {
        brashColor = changeColorOpacity(brashColor, brashColor.getOpacity() / 2);
        fillColor = (fillColor.equals(Color.TRANSPARENT)) ? fillColor : brashColor;
    }

    /**
     * Restores the original color opacity after dragging shapes.
     */
    private void restoreOriginalColorOpacity() {
        brashColor = changeColorOpacity(brashColor, brashColor.getOpacity() * 2);
        fillColor = (fillColor.equals(Color.TRANSPARENT)) ? fillColor : brashColor;
    }

    /**
     * Paints a freehand style dot at the specified (x,y) coordinates with the given color
     */
    private void paintFreeStyle(double x, double y, Color color){
        Circle dot = new Circle(x, y, size, color);
        drawingAreaPane.getChildren().add(dot);
    }


    /**
     * Handles the mouse released event by painting the dragged shape
     */
    @FXML
    void onMouseReleased(MouseEvent event) {
        if (currState != State.PENCIL && currState != State.ERASER){
            cleanDraggedShape();
            paintShape(event.getX(), event.getY());
            whileDragging = false; //indicate that the shape won't be removed
        }
    }

    /**
     * Paints a shape based on the current state and start and end coordinates.
     */
    private void paintShape(double xEnd, double yEnd){

        Shape shape = null;
        switch (currState){
            case LINE:
               shape = new Line(xStart, yStart, xEnd, yEnd);
               break;

            case CIRCLE:
                double radius = Math.sqrt( Math.pow(xEnd - xStart, 2) + Math.pow(yEnd - yStart, 2))/2;
                shape = new Circle((xStart+xEnd)/2, (yStart+yEnd)/2, radius);
                break;

            case RECTANGLE:
                double width = Math.abs(xEnd-xStart);
                double height = Math.abs(yEnd-yStart);
                shape = new Rectangle(Math.min(xStart, xEnd), Math.min(yStart, yEnd), width, height);
                break;

            case TRIANGLE:
                shape = new Polygon(xStart, yStart, (xStart+xEnd)/2, yEnd, xEnd, yStart);
                break;
        }
        if (shape != null){
            shape.setStrokeWidth(size);
            shape.setStroke(brashColor);
            shape.setFill(fillColor);
        }
        drawingAreaPane.getChildren().add(shape);

    }

    /**
     * Removes the last dragged shape from the drawing area.
     */
    private void cleanDraggedShape(){
        if (whileDragging){ //need to remove the last dragged shape
            removeLast();
        }
    }

    /**
     * Adjusts the opacity of a color.
     *
     * @param c   The color to adjust.
     * @param opacity The opacity value.
     * @return The adjusted color.
     */
    private Color changeColorOpacity(Color c, double opacity){
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
    }

}
