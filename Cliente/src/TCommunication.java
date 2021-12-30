import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TCommunication implements Runnable {

    private String toSend;
    private IOnReceivedPacket receivedPacket;

    public TCommunication() {
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress initialAddr = InetAddress.getByName("localhost");
            /*------------------Envia mensagem ao GRDS ---------------*/
            byte[] buf = toSend.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, initialAddr, 5000);
            socket.send(packet);
            System.out.println("Send to GRDS!\nA receber...");
            buf = new byte[4092];
            packet = new DatagramPacket(buf, buf.length);
            socket.setSoTimeout(5000);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            receivedPacket.onReceivedPacket(received);
            /*----------------------------------------------------------------*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setReceivedPacket(IOnReceivedPacket receivedPacket) {
        this.receivedPacket = receivedPacket;
    }

    public void sendCommand(String toSend) {
        this.toSend = toSend;
        this.run();
    }

    public interface IOnReceivedPacket {
        void onReceivedPacket(String packet);
    }
}
