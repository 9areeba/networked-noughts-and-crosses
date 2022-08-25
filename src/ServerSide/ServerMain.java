package ServerSide;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Main server of the game, which listens for connection on port 9090
 * Currently server is running on 'localhost'
 * Allows multiple games to be played simultaneously as two players share the same
 * game object
 */
public class ServerMain {
    private static final int PORT = 9090;

    public static void main(String[] args) {
        try{
            ServerSocket server = new ServerSocket(9090);
            System.out.println("Noughts and Crosses server is up and running, waiting for connections...");
            while(true){
                Game game = new Game();
                new PlayerServer(game, server.accept(), "X").start();
                new PlayerServer(game, server.accept(), "O").start();
            }

        }catch (IOException e){
            e.printStackTrace();
            System.out.print("Error Creating server");
        }
    }
}
