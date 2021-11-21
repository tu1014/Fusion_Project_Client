import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Font.loadFont(getClass().getResource("font/bmjua_ttf.ttf").toString(), 20).getFamily();

        Parent root = FXMLLoader.load(getClass().getResource("fxml/splash.fxml"));
        primaryStage.setScene(new Scene(root));
        // primaryStage.initStyle(StageStyle.UNDECORATED);


        // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.setTitle("Fusion Project");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }



}
