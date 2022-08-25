package ClientSide;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Main class to run the player guis
 */
public class PlayerMain extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        PlayerController playerController = new PlayerController(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerGui.fxml"));
        loader.setController(playerController);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            playerController.handleWindowClosing();
//            Platform.exit();
        });
        stage.show();
    }

    public void stop(){
        stage.close();
    }

    public void setWindowTitle(String title){
        stage.setTitle(title);
    }

}
