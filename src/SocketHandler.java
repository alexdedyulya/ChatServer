import java.io.*;
import java.net.Socket;

/**
 * Created by Alex on 22.11.2017.
 */
public class SocketHandler implements Runnable {

    private final ObjectOutputStream writer;
    private final ObjectInputStream reader;
    private final String id;

    public SocketHandler(String id, Socket socket) throws IOException {
        this.id = id;
        this.writer = new ObjectOutputStream(socket.getOutputStream());
        this.reader = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                MessageRequest messageRequest = (MessageRequest) reader.readObject();
                if (!messageRequest.isOnline()) {
                    SocketPull.getInstance().setOffline(id, messageRequest);
                }
                else if (messageRequest.getText() == null) {
                    SocketPull.getInstance().setUsername(messageRequest);
                }
                SocketPull.getInstance().sendMessage(messageRequest);
            }
        } catch (IOException e) {
            System.out.println("Клиент отключился");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(MessageRequest message) {
        try {
            writer.writeObject(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
