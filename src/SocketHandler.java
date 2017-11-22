import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Alex on 22.11.2017.
 */
public class SocketHandler implements Runnable {

    private final DataOutputStream writer;
    private final DataInputStream reader;


    public SocketHandler(Socket socket) throws IOException {
        this.writer = new DataOutputStream(socket.getOutputStream());
        this.reader = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = reader.readUTF();
                SocketPull.getInstance().sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            writer.writeUTF(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
