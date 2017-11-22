import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 22.11.2017.
 */
public class SocketPull {

    private static volatile SocketPull instance;

    private volatile List<SocketHandler> handlers = new ArrayList<>();


    private SocketPull() {
    }

    public synchronized static SocketPull getInstance() {
        if (instance == null) {
            instance = new SocketPull();
        }

        return instance;
    }

    public void add(Socket socket) throws IOException {
        SocketHandler handler = new SocketHandler(socket);
        handlers.add(handler);
        Thread socketThread = new Thread(handler);
        socketThread.setDaemon(true);
        socketThread.start();
    }

    public void sendMessage(String message) {
        handlers.forEach(socketHandler -> socketHandler.sendMessage(message));
    }

}
