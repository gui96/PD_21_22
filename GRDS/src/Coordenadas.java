import java.io.Serializable;
import java.net.InetAddress;

public class Coordenadas implements Serializable {
    static final long serialVersionUID = 1L;

    private InetAddress address;
    private int port;

    public Coordenadas(InetAddress addr, int port){
        this.port = port;
        address = addr;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
