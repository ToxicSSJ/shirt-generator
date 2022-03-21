package sh.toxic.shirt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sh.toxic.shirt.controller.CanvasController;
import sh.toxic.shirt.graphic.ShirtCanvas;

import java.io.IOException;

public class ShirtApplication extends Application {

    HBox root = new HBox();

    @Override
    public void start(Stage stage) throws IOException {

        ShirtCanvas shirtCanvas = new ShirtCanvas(600, 600);

        FXMLLoader fxmlLoader = new FXMLLoader(ShirtApplication.class.getResource("shirt.fxml"));
        fxmlLoader.setController(new CanvasController(shirtCanvas, stage));
        fxmlLoader.load();

        Scene scene = new Scene(fxmlLoader.getRoot(), Color.rgb(51, 51, 153));
        stage.setTitle("Shirt Generator - v0.1");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.resizableProperty().setValue(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}