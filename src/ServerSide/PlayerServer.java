package ServerSide;
import java.io.*;
import java.net.Socket;
import Utilities.ClientResponses;

/**
 * PlayerServer handles the communication between the two players
 * It uses the game class to move players (moves shown by updating gui in PlayerController class)
 * and check if a player has won
 * PlayerServer also handles if one player leaves mid-game and assigns the players joining their
 * mark (X or O)
 */
public class PlayerServer extends Thread {
    private final Game game;
    private final Socket socket;
    private final String playerSymbol;
    private PlayerServer opponent;
    private BufferedReader in;
    private PrintWriter out;

    public PlayerServer(Game game, Socket socket, String symbol){
        this.game = game;
        this.socket = socket;
        playerSymbol = symbol;
    }

    //Using a new thread to start game
    @Override
    public void run(){
        try {
            startUp();
            processCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //if the player has an opponent
            try {
                //Current player has quit - need to let active opponent know
                if (opponent != null && opponent.out != null) {
                    opponent.out.println("PLAYER_LEFT ");
                }
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startUp(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Welcome " + playerSymbol);
            if(playerSymbol.equals("X")){
                game.setCurrentPlayer(this);
                out.println("STATUS Waiting for another player to join");
            } else{
                opponent = game.currentPlayer;
                opponent.opponent = this;
                opponent.out.println("STATUS Your move");
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void processCommands() throws IOException {
        String playerResponse = in.readLine();

        while(playerResponse != null){
            int indexOfFirstSpace = playerResponse.indexOf(' ');
            String firstWord = playerResponse.substring(0, indexOfFirstSpace);
            ClientResponses command = ClientResponses.valueOf(firstWord);

            switch (command){
                case QUIT:
                    out.println("QUIT ");
                    return; //no more responses sent from server
                case MOVE:
                    handleMoveCommand(removeCommandWord(playerResponse));
            }
            playerResponse = in.readLine();
        }
    }

    private void handleMoveCommand(String location) {
        System.out.println("button " + location + " clicked");
        int locationGridValue = Integer.parseInt(location);

        try {
            game.move(locationGridValue, this);
            out.println("VALID_MOVE ");
            opponent.out.println("OPPONENT_MOVED " + location);
            if (game.hasPlayerWon()) {
                out.println("WIN ");
                out.println("\n");
                opponent.out.println("LOSE ");
                opponent.out.println("\n");

//                out.println(" ");
//                opponent.out.println(" ");
            } else if (game.hasTied()) {
                out.println("TIE ");
                opponent.out.println("TIE ");
            }
            } catch(IllegalStateException e){
                out.println("STATUS " + e.getMessage());
            }
    }

    private String removeCommandWord(String message){
        int indexOfFirstSpace = message.indexOf(' ');
        return  message.substring(indexOfFirstSpace + 1);
    }
    public PlayerServer getOpponent() {
        return opponent;
    }


}





