package sh.toxic.shirt.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import sh.toxic.shirt.HelloApplication;
import sh.toxic.shirt.graphic.ShirtCanvas;

public class CanvasController {

    private ShirtCanvas canvas;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private Button close;
    @FXML private HBox base;
    @FXML private ScrollPane scrollPane;
    @FXML private TabPane tabPane;
    @FXML private AnchorPane variablesPane;
    @FXML private ColorPicker color;

    @FXML private Button ccwRotate;
    @FXML private Button cwRotate;

    @FXML private TextField hipValue;
    @FXML private TextField lengthValue;
    @FXML private TextField backValue;
    @FXML private TextField longValue;


    private Stage stage;

    public CanvasController(ShirtCanvas canvas, Stage stage) {

        this.canvas = canvas;
        this.stage = stage;
    }

    @FXML
    private void initialize() {

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(canvas);

        scrollPane.setContent(anchorPane);

        variablesPane.setStyle("-fx-background-color: #262673");
        tabPane.setStyle("-fx-background-color: #262673");
        anchorPane.setStyle("-fx-background-color: #262673");

        String style = HelloApplication.class.getResource("shirt.css").toExternalForm();

        cwRotate.setOnMouseClicked((event) -> {

            int rotates = canvas.getRotates();

            if(rotates + 1 > 3)
                rotates = 0;
            else
                rotates++;

            canvas.setRotates(rotates);
            canvas.setUpdate(true);

        });

        ccwRotate.setOnMouseClicked((event) -> {

            int rotates = canvas.getRotates();

            if(rotates - 1 < 0)
                rotates = 3;
            else
                rotates--;

            canvas.setRotates(rotates);
            canvas.setUpdate(true);

        });

        base.getStylesheets().add(style);
        tabPane.getStylesheets().add(style);
        scrollPane.getStylesheets().add(style);

        hipValue.setText(String.valueOf(canvas.getData().getHip()));
        lengthValue.setText(String.valueOf(canvas.getData().getLength()));
        backValue.setText(String.valueOf(canvas.getData().getBack()));
        longValue.setText(String.valueOf(canvas.getData().getWidth()));

        numeric(hipValue);
        numeric(lengthValue);
        numeric(backValue);
        numeric(longValue);

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(5), event -> {
            canvas.update();
        }));

        color.setValue(canvas.getColor());
        color.setOnAction((event) -> {

            canvas.setColor(color.getValue());
            canvas.setUpdate(true);

        });

        close.setOnMouseClicked(mouseEvent -> {
            System.exit(-1);
        });

        base.setOnMousePressed(pressEvent -> {
            base.setOnMouseDragged(dragEvent -> {
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

        System.out.println(scrollPane);
    }

    public void numeric(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }

        });
    }

}
