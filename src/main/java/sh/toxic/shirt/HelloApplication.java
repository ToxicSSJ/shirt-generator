package sh.toxic.shirt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sh.toxic.shirt.graphic.ShirtCanvas;

import java.io.IOException;

public class HelloApplication extends Application {

    HBox root = new HBox();

    @Override
    public void start(Stage stage) throws IOException {
        // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        ShirtCanvas shirtCanvas = new ShirtCanvas(500, 500);

        root.getChildren().add(shirtCanvas);
        root.setLayoutX(0);
        root.setLayoutY(0);

        Scene scene = new Scene(root, 500, 500, Color.GREY);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}