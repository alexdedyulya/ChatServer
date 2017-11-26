import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alex on 22.11.2017.
 */
public class SocketPull {

    private static volatile SocketPull instance;

    private volatile Map<String, SocketHandler> handlers = new HashMap<>();
    private Set<String> users = ConcurrentHashMap.newKeySet();


    private SocketPull() {
    }

    public synchronized static SocketPull getInstance() {
        if (instance == null) {
            instance = new SocketPull();
        }

        return instance;
    }

    public void add(Socket socket) throws IOException {
        String id = UUID.randomUUID().toString();
        SocketHandler handler = new SocketHandler(id, socket);
        handlers.put(id, handler);
        Thread socketThread = new Thread(handler);
        socketThread.setDaemon(true);
        socketThread.start();
    }

    public void sendMessage(MessageRequest message) {
        handlers.values().forEach(socketHandler -> socketHandler.sendMessage(message));
    }

    public void setOffline(String id, MessageRequest message) {
        handlers.remove(id);
        users.remove(message.getUsername());
        message.setUsers(new HashSet<>(users));
        handlers.values().forEach(socketHandler -> socketHandler.sendMessage(message));
    }

    public void setUsername(MessageRequest message) {
        users.add(message.getUsername());
        message.setUsers(new HashSet<>(users));
        handlers.values().forEach(socketHandler -> socketHandler.sendMessage(message));
    }

}
