import java.io.Serializable;
import java.net.Socket;

public class Worker implements Serializable {
    static final long serialVersionUID = 1L;
    private Socket s;
    private String address;
    private int udpPort;
    private int tcpPort;

    public Worker(Socket socket){
        s = socket;
    }


    public Socket getSocket() {return s;}

    public int getUdpPort() {
        return udpPort;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setUdpPort(int udpPort) {
        udpPort = udpPort;
    }

    public void setTcpPort(int tcpPort) {
        tcpPort = tcpPort;
    }
}
