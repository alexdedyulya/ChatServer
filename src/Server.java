import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Alex on 22.11.2017.
 */
public class Server {

    private static final int PORT = 8082;

    public void run() {
        try (
                ServerSocket serverSocket = new ServerSocket(PORT);
        ) {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                SocketPull.getInstance().add(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
