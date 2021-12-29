import java.io.Serializable;
import java.net.Socket;

public class Worker implements Serializable {
    static final long serialVersionUID = 1L;
    private Socket s;
    private int toClientPort;
    private int toServerPort;

    public Worker(Socket socket){
        s = socket;
    }

    public int getToClientPort() {return toClientPort;}

    public int getToServerPort() {return toServerPort;}

    public void setToServerPort(int toServerPort) {this.toServerPort = toServerPort;}

    public void setToClientPort(int port) {this.toClientPort = port;}

    public Socket getSocket() {return s;}
}
