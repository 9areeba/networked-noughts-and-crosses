package ClientSide;

import Utilities.ServerResponses;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.*;
import java.net.Socket;

/**
 * PlayerController is responsible for updating the javafx gui.
 * It updates both player's guis by showing where players have placed their marks.
 * Informs the players of when a tie has occurred or which player has won
 * Automatically closes the guis if a player leaves or game has ended
 */
public class PlayerController {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 9090;
    private final PlayerMain mainApp;

    @FXML
    private Label statusMessage;
    @FXML
    private Button b0;
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;
    @FXML
    private Button b4;
    @FXML
    private Button b5;
    @FXML
    private Button b6;
    @FXML
    private Button b7;
    @FXML
    private Button b8;

    private String mySymbol;
    private String opponentSymbol; //need to show opponent mark on this board
    private Button buttonClicked; //button clicked by this player
    private String serverResponse;
    private ClientServer clientServer;

    public PlayerController(PlayerMain mainApp){
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() throws IOException {
        //
        Socket client = new Socket(SERVER_IP, SERVER_PORT);
        clientServer = new ClientServer(client);

        serverResponse = clientServer.readMessage();
        setPlayerSymbols(serverResponse);
        mainApp.setWindowTitle("Noughts and Crosses: Player " + mySymbol);

        play();
    }

    @FXML
    public void handleButtonClicked(ActionEvent event){
        Button button = (Button) event.getSource();
        buttonClicked = button;
        String buttonId = button.getId();
        //paassing grid number
        clientServer.sendMessage("MOVE " + buttonId.charAt(1));
    }

    //handle all incoming command changes here
    public void play() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {


                while (serverResponse != null) {
                    serverResponse = clientServer.readMessage();
                    int indexOfFirstSpace = serverResponse.indexOf(' ');
                    String firstWord = serverResponse.substring(0, indexOfFirstSpace);

                    ServerResponses command = ServerResponses.valueOf(firstWord);
                    switch (command) {
                        case STATUS:
                            setStatusMessage(removeCommandWord(serverResponse));
                            break;
                        case VALID_MOVE:
                            updateGrid("", true);
                            setStatusMessage("Wait for your opponent to move");
                            break;
                        case OPPONENT_MOVED:
                            updateGrid(removeCommandWord(serverResponse), false);
                            setStatusMessage("Opponent moved, your turn");
                            break;
                        case WIN:
                            showDialogBox("WINNER", "Player " + mySymbol + " - you have won!");
                            serverResponse = null;
                            break;
                        case LOSE:
                            showDialogBox("Better luck next time", "Player " + mySymbol + " - Sorry you have lost");
                            serverResponse = null;
                            break;
                        case TIE:
                            showDialogBox("Tie", "It's a tie!");
                            serverResponse = null;
                            break;
                        case PLAYER_LEFT:
                            showDialogBox("Opponent Left", "Your opponent has left, please join another game");
                            serverResponse = null;
                            break;
                        case QUIT:
                            serverResponse = null; //Handling quit for player leaving mid-game
                            break;
                    }
                }
                clientServer.sendMessage("QUIT ");
                clientServer.closeEverything();
                closeMainApp();
            }
        }).start();
    }

    /**
     * Updates player and it's opponent's grid
     */
    private void updateGrid(String response, boolean myMove) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if(myMove){
                    buttonClicked.setText(mySymbol);
                    buttonClicked.setStyle("-fx-text-fill: blue;");
                }
                else{
                    int btnNumber = Integer.parseInt(response);
                    switch(btnNumber){
                        case 0 -> b0.setText(opponentSymbol);
                        case 1 -> b1.setText(opponentSymbol);
                        case 2 -> b2.setText(opponentSymbol);
                        case 3 -> b3.setText(opponentSymbol);
                        case 4 -> b4.setText(opponentSymbol);
                        case 5 -> b5.setText(opponentSymbol);
                        case 6 -> b6.setText(opponentSymbol);
                        case 7 -> b7.setText(opponentSymbol);
                        case 8 -> b8.setText(opponentSymbol);
                    }
                }
            }
        });
    }

    public void setStatusMessage(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusMessage.setText(message);
            }
        });
    }

    public void setPlayerSymbols(String response){
        mySymbol = String.valueOf(response.charAt(8));
        opponentSymbol = mySymbol.equals("X") ? "O" : "X";
    }

    private String removeCommandWord(String message){
        int indexOfFirstSpace = message.indexOf(' ');
        return  message.substring(indexOfFirstSpace + 1);

    }

    private void showDialogBox(String alertTitle, String message){
        Platform.runLater(new Runnable() {
            @Override
            public synchronized void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(alertTitle);
                alert.setHeaderText(message);
                alert.show();
            }
        });
    }

    public void closeMainApp(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainApp.stop();
            }
        });
    }

    /**
     * Handles a user leaving mid-game, which tells player server this player
     * has left
     */
    public void handleWindowClosing() {
        clientServer.sendMessage("QUIT ");
    }
}
