package sh.toxic.shirt.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import sh.toxic.shirt.ShirtApplication;
import sh.toxic.shirt.data.ShirtData;
import sh.toxic.shirt.graphic.ShirtCanvas;

import java.util.function.UnaryOperator;

public class CanvasController {

    private static UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([0-9][0-9]*)?")) {
            return change;
        } else if ("-".equals(change.getText()) ) {
            if (change.getControlText().startsWith("-")) {
                change.setText("");
                change.setRange(0, 1);
                change.setCaretPosition(change.getCaretPosition()-2);
                change.setAnchor(change.getAnchor()-2);
            } else {
                change.setRange(0, 0);
            }
            return change ;
        }
        return null;
    };

    private ShirtCanvas canvas;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private Button close;
    @FXML private Button scalate;
    @FXML private HBox base;
    @FXML private ScrollPane scrollPane;
    @FXML private TabPane tabPane;
    @FXML private AnchorPane variablesPane;
    @FXML private ColorPicker color;

    @FXML private Button ccwRotate;
    @FXML private Button cwRotate;

    @FXML private Button saveHip, subHip, addHip;
    @FXML private Button saveLength, subLength, addLength;
    @FXML private Button saveBack, subBack, addBack;
    @FXML private Button saveLong, subLong, addLong;
    @FXML private Button translate, reset;

    @FXML private ToggleButton reference;

    @FXML private TextField scaleValue;
    @FXML private TextField hipValue;
    @FXML private TextField lengthValue;
    @FXML private TextField backValue;
    @FXML private TextField longValue;

    @FXML private TextField translateX;
    @FXML private TextField translateY;

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

        String style = ShirtApplication.class.getResource("shirt.css").toExternalForm();

        reset.setOnMouseClicked((event) -> {

            canvas.setData(new ShirtData());

            canvas.setScales(1);
            canvas.setRotates(0);
            canvas.setOffsetX(0);
            canvas.setOffsetY(0);

            canvas.setColor(Color.WHITE);
            color.setValue(Color.WHITE);

            scaleValue.setText(String.valueOf(canvas.getScales()));
            hipValue.setText(String.valueOf(canvas.getData().getHip()));
            lengthValue.setText(String.valueOf(canvas.getData().getLength()));
            backValue.setText(String.valueOf(canvas.getData().getBack()));
            longValue.setText(String.valueOf(canvas.getData().getWidth()));

            translateX.setText("0");
            translateY.setText("0");
            canvas.setUpdate(true);

        });

        translate.setOnMouseClicked((event) -> {

            int x = Integer.parseInt(translateX.getText());
            int y = Integer.parseInt(translateY.getText());

            canvas.setOffsetX(x);
            canvas.setOffsetY(y);
            canvas.setUpdate(true);

        });

        saveLong.setOnMouseClicked((event) -> {

            canvas.getData().setWidth(Integer.valueOf(longValue.getText()));
            longValue.setText(String.valueOf(canvas.getData().getWidth()));
            canvas.setUpdate(true);

        });

        subLong.setOnMouseClicked((event) -> {

            canvas.getData().setWidth(canvas.getData().getWidth() - 1);
            longValue.setText(String.valueOf(canvas.getData().getWidth()));
            canvas.setUpdate(true);

        });

        addLong.setOnMouseClicked((event) -> {

            canvas.getData().setWidth(canvas.getData().getWidth() + 1);
            longValue.setText(String.valueOf(canvas.getData().getWidth()));
            canvas.setUpdate(true);

        });

        saveBack.setOnMouseClicked((event) -> {

            canvas.getData().setBack(Integer.valueOf(backValue.getText()));
            backValue.setText(String.valueOf(canvas.getData().getBack()));
            canvas.setUpdate(true);

        });

        subBack.setOnMouseClicked((event) -> {

            canvas.getData().setBack(canvas.getData().getBack() - 1);
            backValue.setText(String.valueOf(canvas.getData().getBack()));
            canvas.setUpdate(true);

        });

        addBack.setOnMouseClicked((event) -> {

            canvas.getData().setBack(canvas.getData().getBack() + 1);
            backValue.setText(String.valueOf(canvas.getData().getBack()));
            canvas.setUpdate(true);

        });

        saveLength.setOnMouseClicked((event) -> {

            canvas.getData().setLength(Integer.valueOf(lengthValue.getText()));
            lengthValue.setText(String.valueOf(canvas.getData().getLength()));
            canvas.setUpdate(true);

        });

        subLength.setOnMouseClicked((event) -> {

            canvas.getData().setLength(canvas.getData().getLength() - 1);
            lengthValue.setText(String.valueOf(canvas.getData().getLength()));
            canvas.setUpdate(true);

        });

        addLength.setOnMouseClicked((event) -> {

            canvas.getData().setLength(canvas.getData().getLength() + 1);
            lengthValue.setText(String.valueOf(canvas.getData().getLength()));
            canvas.setUpdate(true);

        });

        saveHip.setOnMouseClicked((event) -> {

            canvas.getData().setHip(Integer.valueOf(hipValue.getText()));
            hipValue.setText(String.valueOf(canvas.getData().getHip()));
            canvas.setUpdate(true);

        });

        addHip.setOnMouseClicked((event) -> {

            canvas.getData().setHip(canvas.getData().getHip() + 1);
            hipValue.setText(String.valueOf(canvas.getData().getHip()));
            canvas.setUpdate(true);

        });

        subHip.setOnMouseClicked((event) -> {

            canvas.getData().setHip(canvas.getData().getHip() - 1);
            hipValue.setText(String.valueOf(canvas.getData().getHip()));
            canvas.setUpdate(true);

        });

        reference.setOnMouseClicked((event) -> {

            canvas.setReference(reference.isSelected());
            canvas.setUpdate(true);

        });

        scalate.setOnMouseClicked((event) -> {

            int scales = canvas.getScales();
            int input = Integer.valueOf(scaleValue.getText());

            if(input < 0) {

                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid scale value", ButtonType.OK);
                alert.showAndWait();

                scaleValue.setText(String.valueOf(scales));

            } else {

                canvas.setScales(input);
                canvas.setUpdate(true);

            }

        });

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

        scaleValue.setText(String.valueOf(canvas.getScales()));
        hipValue.setText(String.valueOf(canvas.getData().getHip()));
        lengthValue.setText(String.valueOf(canvas.getData().getLength()));
        backValue.setText(String.valueOf(canvas.getData().getBack()));
        longValue.setText(String.valueOf(canvas.getData().getWidth()));

        translateX.setText("0");
        translateY.setText("0");

        number(translateX);
        number(translateY);
        numeric(scaleValue);
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

    }

    public void numeric(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }

        });
    }

    public void number(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    }

}
