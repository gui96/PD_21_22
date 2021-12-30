import java.io.Serializable;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Worker implements Serializable {
    static final long serialVersionUID = 1L;
    private Socket s;
    private String address;
    private int udpPort;
    private int tcpPort;
    private Calendar Date;

    public Worker(String address, int udpPort, int tcpPort) {
        this.address = address;
        this.udpPort = udpPort;
        this.tcpPort = tcpPort;
        this.Date = GregorianCalendar.getInstance();
    }

    public int getUdpPort() {return udpPort;}

    public int getTcpPort() {return tcpPort;}

    public String getAddress() {
        return address;
    }

    public Calendar getDate() {
        return Date;
    }

    public void setUdpPort(int udpPort) {this.udpPort = udpPort;}

    public void setTcpPort(int tcpPort) {this.tcpPort = tcpPort;}

    public Socket getSocket() {return s;}

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(Calendar date) {
        Date = date;
    }


}
