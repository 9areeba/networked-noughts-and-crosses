package ClientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class handles the server side of the client.
 * It creates the input and output streams of the socket, which are used to send and receive data
 * from the server (PlayerServer class)
 */
public class ClientServer {

    PlayerController controller;
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    ClientServer(Socket socket){
        try {
            client = socket;
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Couldn't connect player");
            closeEverything();
        }
    }

    public void closeEverything() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String readMessage(){
        String resposne = null;
        try{
            resposne = in.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        return resposne;
    }
}
